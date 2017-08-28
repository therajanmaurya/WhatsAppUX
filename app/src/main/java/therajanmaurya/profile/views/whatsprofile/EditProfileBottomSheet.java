package therajanmaurya.profile.views.whatsprofile;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import therajanmaurya.profile.views.R;
import therajanmaurya.profile.views.utils.CheckSelfPermissionAndRequest;
import therajanmaurya.profile.views.utils.ConstantKeys;
import therajanmaurya.profile.views.utils.FileUtils;
import therajanmaurya.profile.views.utils.ImageLoaderUtils;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public class EditProfileBottomSheet extends BottomSheetDialogFragment
        implements EditProfileContract.View, View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PHOTO_FROM_GALLERY = 2;

    @BindView(R.id.iv_profile_picture)
    CircleImageView ivProfilePicture;

    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.tv_image_name)
    TextView tvImageName;

    @BindView(R.id.btn_choose_select_photo)
    Button btnChooseSelectPhoto;

    @BindView(R.id.ll_camera)
    LinearLayout llCamera;

    @BindView(R.id.ll_gallery)
    LinearLayout llGallery;

    @BindView(R.id.ll_remove_photo)
    LinearLayout llRemovePhoto;

    @BindView(R.id.ll_edit_action_form)
    LinearLayout llEditActionForm;

    @BindView(R.id.ll_edit_actions)
    LinearLayout llEditActions;

    @BindView(R.id.tv_select_file)
    TextView tvSelectFile;

    @BindView(R.id.tv_image_size)
    TextView tvImageSize;

    @BindView(R.id.btn_upload_photo)
    Button btnUploadPhoto;

    View rootView;

    private BottomSheetBehavior behavior;
    private File file;
    private EditAction editAction;
    private RefreshProfilePhoto refreshProfilePhoto;
    private Bitmap imageBitmap;
    private byte[] imageByte;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(),
                R.layout.bottom_sheet_whats_app_profile, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ButterKnife.bind(this, rootView);

        showUserInterface();

        return dialog;
    }

    @Override
    public void onClick(View v) {
        llEditActions.setVisibility(View.GONE);
        llEditActionForm.setVisibility(View.VISIBLE);
        switch (v.getId()) {
            case R.id.ll_gallery:
                editAction = EditAction.GALLERY;
                tvHeader.setText(R.string.gallery);
                btnChooseSelectPhoto.setText(R.string.choose_file);
                btnUploadPhoto.setText(R.string.upload);
                btnChooseSelectPhoto.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_attach_file_black_24dp, 0, 0, 0);
                btnChooseSelectPhoto.setVisibility(View.VISIBLE);
                tvImageName.setText(R.string.no_file_selected_yet);
                tvSelectFile.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_camera:
                editAction = EditAction.CAMERA;
                tvHeader.setText(R.string.camera);
                btnChooseSelectPhoto.setText(R.string.take_photo);
                btnChooseSelectPhoto.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_camera_enhance_black_24dp, 0, 0, 0);
                btnUploadPhoto.setText(R.string.upload);
                btnChooseSelectPhoto.setVisibility(View.VISIBLE);
                tvImageName.setText(R.string.no_file_selected_yet);
                tvSelectFile.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_remove_photo:
                editAction = EditAction.REMOVE;
                tvHeader.setText(R.string.remove_photo);
                btnChooseSelectPhoto.setVisibility(View.GONE);
                tvSelectFile.setVisibility(View.GONE);
                tvImageName.setText(R.string.are_sure_want_remove_photo);
                btnUploadPhoto.setText(R.string.remove);
                break;
        }
    }

    @Override
    public void showUserInterface() {
        llCamera.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        llRemovePhoto.setOnClickListener(this);

        ImageLoaderUtils.loanDrawableIntoImageView(getActivity(),
                ivProfilePicture, R.drawable.cheese_4);
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        llEditActions.setVisibility(View.VISIBLE);
        llEditActionForm.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_upload_photo)
    void uploadPhoto() {
        switch (editAction) {
            case GALLERY:
                if (file != null) {
                    refreshProfilePhoto.showCameraPhoto(file);
                } else {
                    Toast.makeText(getActivity(), R.string.gallery_photo_not_changed,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case CAMERA:
                if (imageBitmap != null) {
                    refreshProfilePhoto.showGalleryPhoto(imageBitmap);
                } else {
                    Toast.makeText(getActivity(), R.string.camera_photo_not_changed,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case REMOVE:
                refreshProfilePhoto.deletePhoto();
                Toast.makeText(getActivity(), R.string.photo_removed, Toast.LENGTH_LONG).show();
                break;
        }
        dismiss();
    }

    @OnClick(R.id.btn_choose_select_photo)
    void chooseSelectDeletePhoto() {
        switch (editAction) {
            case CAMERA:
                checkWriteExternalStorageAndCameraPermission();
                break;
            case GALLERY:
                checkReadExternalStoragePermission();
                break;
        }
    }

    public void setRefreshProfilePhoto(RefreshProfilePhoto refreshProfilePhoto) {
        this.refreshProfilePhoto = refreshProfilePhoto;
    }

    @Override
    public void checkWriteExternalStorageAndCameraPermission() {
        if (CheckSelfPermissionAndRequest.checkMultiplePermissions(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            openCamera();
        } else {
            requestWriteExternalStorageAndCameraPermission();
        }
    }

    @Override
    public void checkReadExternalStoragePermission() {
        if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            viewGallery();
        } else {
            requestReadExternalStoragePermission();
        }
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void viewGallery() {
        Intent intentDocument = new Intent(Intent.ACTION_GET_CONTENT);
        intentDocument.setType("image/*");
        startActivityForResult(intentDocument, REQUEST_PHOTO_FROM_GALLERY);
    }

    @Override
    public void showImageSizeExceededOrNot() {
        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
        if (fileSize > 512) {
            btnUploadPhoto.setEnabled(false);
            tvImageSize.setVisibility(View.VISIBLE);
        } else {
            btnUploadPhoto.setEnabled(true);
            tvImageSize.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestWriteExternalStorageAndCameraPermission() {
        CheckSelfPermissionAndRequest.requestPermissions(
                (WhatsAppProfileActivity) getActivity(),
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                ConstantKeys.PERMISSION_REQUEST_ALL,
                new String[]{
                        getString(R.string.dialog_message_write_permission_denied_prompt),
                        getString(R.string.
                                dialog_message_camera_permission_for_portrait_denied_prompt)}
                ,
                new String[]{
                        getString(R.string.dialog_message_write_permission_never_ask_again),
                        getString(R.string
                                .dialog_message_camera_permission_for_portrait_never_ask_again)}
                ,
                new String[]{ConstantKeys.PERMISSIONS_WRITE_EXTERNAL_STORAGE_STATUS,
                        ConstantKeys.PERMISSIONS_CAMERA_STATUS}
        );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void requestReadExternalStoragePermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                (WhatsAppProfileActivity) getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE,
                getResources().getString(
                        R.string.dialog_message_read_permission_denied_prompt),
                getResources().getString(R.string.dialog_message_read_permission_never_ask_again),
                ConstantKeys.PERMISSIONS_READ_EXTERNAL_STORAGE_STATUS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantKeys.PERMISSION_REQUEST_ALL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    String permissionDeniedMessage = getString(R.string.permission_denied_write) +
                            getString(R.string.permission_denied_camera);
                    Snackbar snackbar = Snackbar.make(
                            getActivity().findViewById(android.R.id.content),
                            permissionDeniedMessage, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;

            case ConstantKeys.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewGallery();
                } else {
                    Snackbar snackbar = Snackbar.make(
                            getActivity().findViewById(android.R.id.content),
                            getString(R.string.permission_denied_read), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            file = FileUtils.createFile(getString(R.string.app_name), "profile.png");
            FileUtils.saveBitmap(imageBitmap, file);
            tvImageName.setText(file.getName());
            ivProfilePicture.setImageBitmap(imageBitmap);

            showImageSizeExceededOrNot();

        } else if (requestCode == REQUEST_PHOTO_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            file = new File(FileUtils.getPathReal(getActivity(), uri));
            tvImageName.setText(file.getName());
            Glide.with(getActivity()).load(uri).asBitmap().into(ivProfilePicture);

            showImageSizeExceededOrNot();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
