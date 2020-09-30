package com.ath.floppy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    ShapeableImageView userImage;
    TextView userName;
    Button addPic;
    Button signOut;
    Button favorite;
    File picture;
    JSONObject objPic;
    ParseFile file;


    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userName = findViewById(R.id.userName);
        addPic = findViewById(R.id.settings);
        signOut = findViewById(R.id.sign_out);
        favorite = findViewById(R.id.button_fav);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, FavoriteListActivity.class);
                startActivity(intent);
            }
        });

        userImage = findViewById(R.id.user_header);
        float radius = getResources().getDimension(R.dimen.default_corner_radius);
        userImage.setShapeAppearanceModel(userImage.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                .build());

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(UserActivity.this);
            }
        });

        if(isConnected()) {
            getCurrentUser();
        }
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                if (currentUser != null) {
                    currentUser.logOut();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(UserActivity.this, MainActivity.class));
//        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goBack(View v) {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        userImage.setImageBitmap(selectedImage);
//                    }
//
//                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                userImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                picture = new File(picturePath);
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                BufferedInputStream in = null;
                                try {
                                    in = new BufferedInputStream(new FileInputStream(picture));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                int read;
                                byte[] buff = new byte[1024];
                                try {
                                    while ((read = in.read(buff)) > 0) {
                                        out.write(buff, 0, read);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    out.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                byte[] pdfBytes = out.toByteArray();

                                // Create the ParseFile
                                file = new ParseFile(picture.getName(), pdfBytes);
                                cursor.close();

                                updateUser();
                            }
                        }

                    }
                    break;
            }
        }
    }

    public void getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            userName.setText(currentUser.getUsername());
            ParseFile file = (ParseFile) currentUser.get("userCard");
            if (file != null) {
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                            userImage.setImageBitmap(bitmapImage);
                        } else {
                            Log.i("info", e.getMessage());
                        }
                    }
                });
            }

        } else {
            // show the signup or login screen
        }
    }

    public void updateUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            currentUser.put("userCard", file);

            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // Here you can handle errors, if thrown. Otherwise, "e" should be null
                }
            });
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

//                if (options[item].equals("Take Photo")) {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
//
//                } else
                if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean isConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;

            return networkInfo.isConnected(); // && networkInfo.getType() == ConnectivityManager.TYPE_WIFI ;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
}