package com.apkdv.mvvmfast.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.apkdv.mvvmfast.loader.GlideApp;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GlideUtil {

    private static GlideUtil imageLoadUtil;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (imageLoadUtil == null) {
            synchronized (GlideUtil.class) {
                if (imageLoadUtil == null)
                    imageLoadUtil = new GlideUtil();
                return imageLoadUtil;
            }

        }
        return imageLoadUtil;
    }


    public void loadUrlSource(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {

        GlideApp.with(imageView.getContext())
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(imageView);

    }
    public void loadRoundImg(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        Glide.with(imageView.getContext())
                .load(imgUrl)
                .thumbnail(loadCircleTransform(imageView.getContext(),placeHolder))
                .thumbnail(loadCircleTransform(imageView.getContext(),placeHolder))
                .apply(new RequestOptions().circleCrop())
                .into(imageView);

    }
    public void loadUrlRadius(ImageView imageView, String imgUrl, int radius,@DrawableRes int Res) {
        Glide.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(Res).error(Res)
                        .fitCenter()
                )
                .load(imgUrl)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions().placeholder(Res).error(Res))
                .into(imageView);

    }
    public void loadUrlSourceCenterCrop(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        Glide.with(Utils.getApp())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(placeHolder).error(placeHolder)
                        .fitCenter()
                )
                .load(imgUrl)
                .centerCrop()
                .into(imageView);
    }


    public void loadUrl(ImageView imageView, @DrawableRes int imgUrl) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadUrl(ImageView imageView, String imgUrl, int placeHolder) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder).dontAnimate())
                .into(imageView);
    }

    public void loadUrlCrossFade(ImageView imageView, String imgUrl) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    public void loadUrlCrossFade(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().placeholder(placeHolder))
                .into(imageView);
    }


    public void loadImgUrlWithoutDefault(ImageView imageView, String imgUrl) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadImgUrlWithoutDefault(ImageView imageView, @DrawableRes int imgUrl) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .into(imageView);
    }

    public void loadGifUrl(ImageView imageView, @DrawableRes int gifRes) {
        Glide.with(Utils.getApp())
                .asGif()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .load(gifRes)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop())
                .into(imageView);
    }

    public void loadGifUrl(ImageView imageView, File file) {
        Glide.with(Utils.getApp()).asGif().load(file).into(imageView);
    }

    public void loadGifUrl(ImageView imageView, File file, @DrawableRes int Res) {
        Glide.with(Utils.getApp())
                .load(file)
                .apply(new RequestOptions().placeholder(Res).error(Res))
                .into(imageView);
    }

    public void loadImgUrl(ImageView imageView, String imgUrl, @DrawableRes int placeHolder) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .apply(new RequestOptions().placeholder(placeHolder).error(placeHolder).dontAnimate())
                .into(imageView);

    }

    public void loadUrlSimpleTarget(String imgUrl, final GlideUtilSimpleTarget target) {
        Glide.with(Utils.getApp()).load(imgUrl).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                target.onResourceReady(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                target.onLoadFailed();
            }
        });

    }

    public void loadUrlDiskCacheNONE(ImageView imageView, String url, @DrawableRes int placeHolder) {
        Glide.with(Utils.getApp())
                .load(url)
                .apply(new RequestOptions().error(placeHolder).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    public interface GlideUtilSimpleTarget {
        void onResourceReady(Drawable resource);

        void onLoadFailed();
    }

    public Bitmap loadUrlgetBitmap(String imgUrl, int size) throws InterruptedException, ExecutionException, TimeoutException {

        return Glide.with(Utils.getApp())
                .asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(size, size))
                .submit()
                .get(200, TimeUnit.MILLISECONDS);// 最大等待200ms
    }

    public void loadUrl(ImageView image, String imgUrl, @DrawableRes int defaultResId, int width, int height) {

        Glide.with(Utils.getApp()).asBitmap()
                .load(imgUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(defaultResId)
                        .error(defaultResId)
                        .centerCrop()
                        .override(width, height))
                .into(image);
    }

    public void loadUrltoCache(String imgUrl, int size) {
        Glide.with(Utils.getApp())
                .load(imgUrl)
                .submit(size, size);
    }

    private RequestBuilder<Drawable> loadRoundedTransform(Context context, @DrawableRes int placeholderId, int radius) {
        return Glide.with(context)
                .load(placeholderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(radius)))
                .apply(new RequestOptions()
                        .transform(new MultiTransformation<>(new CenterCrop(), new CircleCrop())));

    }
    private RequestBuilder<Drawable> loadCircleTransform(Context context, @DrawableRes int placeholderId) {
        return Glide.with(context)
                .load(placeholderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new CircleCrop()));

    }
}
