package com.commercial.tuds.earnandpay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.commercial.tuds.earnandpay.DiscountCardActivity;
import com.commercial.tuds.earnandpay.InvestMoney;
import com.commercial.tuds.earnandpay.R;
import com.commercial.tuds.earnandpay.RechargeActivity;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {


    private ArrayList<String> urls;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter( Context context,ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View imageLayout = inflater.inflate(R.layout.slide, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        try {
            Glide.with(imageView.getContext())
                    .load(urls.get(position))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (position)
        {
            case 0:
                imageView.setOnClickListener(v -> context.startActivity(new Intent(context, InvestMoney.class)));
                break;
            case 1:
                imageView.setOnClickListener(v -> context.startActivity(new Intent(context, DiscountCardActivity.class)));
                break;
            case 2:
                imageView.setOnClickListener(v -> context.startActivity(new Intent(context, RechargeActivity.class)));
                break;
            default:
                break;

        }
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}