package in.co.freshstart.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FreshPostAdapter extends PagerAdapter {

    Context context;
    List<Posts> postList;
    LayoutInflater inflater;

    public FreshPostAdapter(Context context, List<Posts> postList) {
        this.context = context;
        this.postList = postList;
        inflater = LayoutInflater.from(context);

    }

    public FreshPostAdapter() {

    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager2)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.all_posts_layout, container, false);


        TextView postUsername = (TextView) view.findViewById(R.id.timeline_post_profile_name);
        CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.timeline_post_profile_image);
        TextView post_time = (TextView) view.findViewById(R.id.timeline_post_time);
        TextView post_date = (TextView) view.findViewById(R.id.timeline_post_date);
        TextView Description = (TextView) view.findViewById(R.id.timeline_post_description);
        ImageView PostImage = (ImageView) view.findViewById(R.id.timeline_post_image);

        postUsername.setText(postList.get(position).getUsername());
        Picasso.with(context.getApplicationContext()).load(postList.get(position).getPostimage()).into(PostImage);
        post_time.setText(postList.get(position).getTime());
        post_date.setText(postList.get(position).getDate());
        Picasso.with(context.getApplicationContext()).load(postList.get(position).getProfileimage()).into(profileImage);
        Description.setText(postList.get(position).getDescription());

        container.addView(view);
        return view;

    }
}
