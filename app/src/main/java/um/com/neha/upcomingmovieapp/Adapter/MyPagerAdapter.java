package um.com.neha.upcomingmovieapp.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import um.com.neha.upcomingmovieapp.R;

/**
 * Created by Mangesh on 1/31/2018.
 */

public class MyPagerAdapter extends PagerAdapter {

    private List<String> images;
    private LayoutInflater inflater;
    private Context context;

    public MyPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_layout, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Picasso.with(context)
                .load(images.get(position))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
