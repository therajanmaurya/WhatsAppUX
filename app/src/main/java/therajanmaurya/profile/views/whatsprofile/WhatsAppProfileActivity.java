package therajanmaurya.profile.views.whatsprofile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import therajanmaurya.profile.views.R;
import therajanmaurya.profile.views.utils.CheckSelfPermissionAndRequest;
import therajanmaurya.profile.views.utils.ConstantKeys;
import therajanmaurya.profile.views.utils.ImageLoaderUtils;
import therajanmaurya.profile.views.utils.Utils;

/**
 * @author Rajan Maurya
 *         On 27/08/17.
 */
public class WhatsAppProfileActivity extends AppCompatActivity implements RefreshProfilePhoto {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_what_picture)
    ImageView ivWhatsAppPicture;

    @BindView(R.id.tv_removed_photo)
    TextView tvRemovedPhoto;

    private Bitmap bitmap;
    private byte[] imageBytes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_profile);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        setActionBarTitle(getString(R.string.profile_photo));
        showBackButton();
    }

    public void checkExternalStoragePermission() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ImageLoaderUtils.shareImage(this, ivWhatsAppPicture);
        } else {
            requestPermission();
        }
    }

    public void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_write_permission_for_share_denied_prompt),
                getResources().getString(
                        R.string.dialog_message_write_permission_for_share_never_ask_again),
                ConstantKeys.PERMISSIONS_WRITE_EXTERNAL_STORAGE_STATUS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantKeys.PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageLoaderUtils.shareImage(this, ivWhatsAppPicture);
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            getString(R.string.permission_denied_write), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null && getTitle() != null) {
            setTitle(title);
        }
    }

    public void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_profile_edit:
                EditProfileBottomSheet profileBottomSheet =
                        new EditProfileBottomSheet();
                profileBottomSheet.setRefreshProfilePhoto(this);
                profileBottomSheet.show(getSupportFragmentManager(),
                        getString(R.string.profile_photo));
                return true;
            case R.id.menu_profile_share:
                checkExternalStoragePermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_whats_app, menu);
        Utils.setToolbarIconColor(this, menu, R.color.white);
        return super.onCreateOptionsMenu(menu);
    }

    public void showProfilePhoto(boolean status) {
        if (status) {
            ivWhatsAppPicture.setVisibility(View.VISIBLE);
            tvRemovedPhoto.setVisibility(View.GONE);
        } else {
            ivWhatsAppPicture.setVisibility(View.GONE);
            tvRemovedPhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCameraPhoto(File file) {
        Uri uri = Uri.fromFile(file);
        ImageLoaderUtils.loanUriIntoImageView(this, ivWhatsAppPicture, uri);
        showProfilePhoto(true);
    }

    @Override
    public void showGalleryPhoto(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        ImageLoaderUtils.loadBitmapIntoImage(this, ivWhatsAppPicture, stream.toByteArray());
        showProfilePhoto(true);
    }

    @Override
    public void deletePhoto() {
        showProfilePhoto(false);
        bitmap = null;
    }
}
