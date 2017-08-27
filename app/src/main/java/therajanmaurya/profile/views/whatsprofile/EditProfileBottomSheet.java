package therajanmaurya.profile.views.whatsprofile;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import therajanmaurya.profile.views.R;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public class EditProfileBottomSheet extends BottomSheetDialogFragment
        implements EditCustomerProfileContract.View, View.OnClickListener {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_PHOTO_FROM_GALLERY = 2;

    @BindView(R.id.iv_customer_picture)
    CircleImageView ivCustomerPicture;

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
    private String customerIdentifier;
    private File file;
    private EditAction editAction;

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
                break;
            case R.id.ll_camera:
                editAction = EditAction.CAMERA;
                tvHeader.setText(R.string.camera);
                btnChooseSelectPhoto.setText(R.string.take_photo);
                btnChooseSelectPhoto.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_camera_enhance_black_24dp, 0, 0, 0);
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
    }

    @Override
    public void setCustomerIdentifier(String customerIdentifier) {

    }

    @Override
    public void checkWriteExternalStoragePermission() {

    }

    @Override
    public void checkReadExternalStoragePermission() {

    }

    @Override
    public void openCamera() {

    }

    @Override
    public void viewGallery() {

    }

    @Override
    public void showImageSizeExceededOrNot() {

    }

    @Override
    public void requestWriteExternalStorageAndCameraPermission() {

    }

    @Override
    public void requestReadExternalStoragePermission() {

    }

    @Override
    public void showPortraitUploadedSuccessfully() {

    }

    @Override
    public void showPortraitDeletedSuccessfully() {

    }

    @Override
    public void showProgressDialog(String message) {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showMessage(String message) {

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
