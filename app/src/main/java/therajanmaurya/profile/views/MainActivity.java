package therajanmaurya.profile.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import therajanmaurya.profile.views.whatsprofile.WhatsAppProfileActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.setTitle(getString(R.string.app_name));
    }

    @OnClick(R.id.btn_whats_app)
    void onClickWhatsAppProfile() {
        startActivity(new Intent(this, WhatsAppProfileActivity.class));
    }
}
