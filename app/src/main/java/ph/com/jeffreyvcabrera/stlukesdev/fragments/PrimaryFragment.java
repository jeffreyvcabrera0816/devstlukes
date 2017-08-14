package ph.com.jeffreyvcabrera.stlukesdev.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ph.com.jeffreyvcabrera.stlukesdev.R;

/**
 * Created by Jeffrey on 2/21/2017.
 */

public class PrimaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.primary_fragment,null);
    }
}
