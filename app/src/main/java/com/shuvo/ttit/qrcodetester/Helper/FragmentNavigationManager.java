package com.shuvo.ttit.qrcodetester.Helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shuvo.ttit.qrcodetester.BuildConfig;
import com.shuvo.ttit.qrcodetester.R;
import com.shuvo.ttit.qrcodetester.TreeMenu;
import com.shuvo.ttit.qrcodetester.fragments.FragmentContent;

public class FragmentNavigationManager implements NavigationManager{

    private static FragmentNavigationManager mInstance;
    private FragmentManager fragmentManager;
    private TreeMenu treeMenu;

    public static FragmentNavigationManager getmInstance (TreeMenu treeMenu) {
        if (mInstance == null) {
            mInstance = new FragmentNavigationManager();
        }
        mInstance.configure(treeMenu);
        return mInstance;
    }

    private void configure(TreeMenu treeMenu) {

        this.treeMenu = treeMenu;
        this.fragmentManager = treeMenu.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title) {

        showFragment(FragmentContent.newInstance(title),false);
    }

    private void showFragment(Fragment fragment,boolean b) {
        FragmentManager fm = fragmentManager;
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.frame_container,fragment);
        ft.addToBackStack(null);
        if (b || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        }
        else {
            ft.commit();
        }
        fm.executePendingTransactions();
    }
}
