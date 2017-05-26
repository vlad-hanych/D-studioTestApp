package com.foxy_corporation.d_studiotestapp.view.authorization;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.foxy_corporation.d_studiotestapp.R;
import com.foxy_corporation.d_studiotestapp.utils.Constants;
import com.foxy_corporation.d_studiotestapp.utils.PreferencesUtils;
import com.foxy_corporation.d_studiotestapp.view.authorization.loginization.LoginizationFragment;
import com.foxy_corporation.d_studiotestapp.view.authorization.registration.RegistrationFragment;
import com.foxy_corporation.d_studiotestapp.view.inside.InsideActivity;

public class AuthorizationActivity extends AppCompatActivity implements AuthorizationView {
    private FragmentManager mSupportFragmentManager;

    private static enum Fragments { LoginizationFragment, RegistrationFragment }

    private Fragments mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorization);

        PreferencesUtils myPreferencesUtils = PreferencesUtils.getInstance(this);

        if (myPreferencesUtils.getIsUserLoggedIn())
            setLoggedIn(myPreferencesUtils.getAccessToken()); /// TODO Before setting loged in check access token validation somehow... GET MY USER DATA!
        else {
            mSupportFragmentManager = getSupportFragmentManager();

            putFragment(LoginizationFragment.class.getName());

            mCurrentFragment = Fragments.LoginizationFragment;
        }
    }

    private void putFragment (String fragmentClassName) {
        /*Log.d("AuthorizationActivity...putFragment: ", fragmentClassName);*/

        FragmentTransaction ft = mSupportFragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        /// TODO тут подумати над логікою перестановки фрагментів
        ft.replace(R.id.framLayRootContainer_AM, Fragment.instantiate(this, fragmentClassName), fragmentClassName);
        ft.commit();
    }

    @Override
    public void setResult(boolean isResultSuccessful, String resultString) {

    }

    @Override
    public void setMessage(String message) {

    }

    public void setRegistration () {
        /*Log.d("AuthorizationActivity...setRegistration: ", "!");*/

        putFragment(RegistrationFragment.class.getName());

        mCurrentFragment = Fragments.RegistrationFragment;
    }

    @Override
    public void setLoggedIn(String accessToken) {
        /*Log.d("AuthorizationActivity...setLoggedIn: ", "!");*/

        Intent insideActivityIntent = new Intent(this, InsideActivity.class);

        insideActivityIntent.putExtra(Constants.ACCESS_TOKEN, accessToken);

        startActivity(insideActivityIntent);

        finish();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment == Fragments.RegistrationFragment) {
            putFragment(LoginizationFragment.class.getName());
            mCurrentFragment = Fragments.LoginizationFragment;
        }
        else
            finish();
    }
}
