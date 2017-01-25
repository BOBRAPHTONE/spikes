package com.novoda.peepz;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeepView extends FrameLayout {

    @BindView(R.id.peep_text_name)
    TextView nameTextView;

    @BindView(R.id.peep_image)
    ImageView imageView;

    @BindView(R.id.peep_online_status)
    View onlineStatusView;

    public PeepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_peep, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightPixels = ((ViewGroup) getParent()).getHeight();

        int peekPx = getResources().getDimensionPixelSize(R.dimen.size_peek);
        int leftOverHeightPx = heightPixels - peekPx;

        int columns = getResources().getInteger(R.integer.spans);
        int desiredHeight = leftOverHeightPx / columns;

        int desiredHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desiredHeightMeasureSpec);
    }

    public void bind(Peep peep) {
        nameTextView.setText(peep.name());
        Glide.with(getContext()).load(peep.image().payload()).into(imageView);

        int onlineStatusColor = getOnlineStatusColor(peep);
        onlineStatusView.setBackgroundColor(onlineStatusColor);
    }

    @ColorInt
    private int getOnlineStatusColor(Peep peep) {
        switch (peep.onlineStatus()) {
            case FRESH:
                return ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
            case STALE:
                return ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            case OFFLINE:
            default:
                return ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
        }
    }

}
