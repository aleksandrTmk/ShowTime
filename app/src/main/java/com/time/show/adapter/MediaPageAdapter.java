package com.time.show.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.time.show.R;
import com.time.show.fragment.MediaFragment;

/**
 * Fragment pager adapter for displaying media items.
 */
public class MediaPageAdapter extends FragmentStatePagerAdapter {
    /**
     * Adapter position of movie fragment
     */
    public static final int POS_FRAGMENT_MOVIE = 0;
    /**
     * Adapter position of tv show fragment
     */
    public static final int POS_FRAGMENT_TV_SHOW = 1;

    /**
     * Interface to detect when a fragment has been instantiated, so that it can be added to a list
     * from which we can grab an instance of the fragment from the view pager.
     *
     * Note: see MediaPageAdapter#instantiateItem() java doc for more information
     */
    public interface OnFragmentStateListener{
        void onFragmentInitialized(int pos);
    }

    private static final int MAX_NUM_FRAGMENTS = 2;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private OnFragmentStateListener onFragmentStateListener;
    private Context context;

    public MediaPageAdapter(FragmentManager fm, Context context, OnFragmentStateListener listener) {
        super(fm);
        this.context = context;
        this.onFragmentStateListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return MediaFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MAX_NUM_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (context == null){
            return "";
        }
        switch (position) {
            case POS_FRAGMENT_MOVIE:
                return context.getString(R.string.section_movies);
            case POS_FRAGMENT_TV_SHOW:
                return context.getString(R.string.section_tv);
        }
        return "";
    }

    /**
     * The correct way to reference fragments from a view pager.
     *
     * SOLUTION: ( Feels hacky but works )
     * Notify whoever cares that a fragment has been instantiated. Therefore, getRegisteredFragment()
     * will return a result. This is used to get media into the MediaFragment.
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);

        // see above java doc for explanation of why this exists
        onFragmentStateListener.onFragmentInitialized(position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
