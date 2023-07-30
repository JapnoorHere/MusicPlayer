// Generated by data binding compiler. Do not edit!
package com.droidbytes.musicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.droidbytes.musicplayer.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class SongItemBinding extends ViewDataBinding {
  @NonNull
  public final ImageView icon;

  @NonNull
  public final LottieAnimationView lottieView;

  @NonNull
  public final ImageView more;

  @NonNull
  public final TextView singerName;

  @NonNull
  public final TextView songName;

  protected SongItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView icon, LottieAnimationView lottieView, ImageView more, TextView singerName,
      TextView songName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.icon = icon;
    this.lottieView = lottieView;
    this.more = more;
    this.singerName = singerName;
    this.songName = songName;
  }

  @NonNull
  public static SongItemBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.song_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static SongItemBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<SongItemBinding>inflateInternal(inflater, R.layout.song_item, root, attachToRoot, component);
  }

  @NonNull
  public static SongItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.song_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static SongItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<SongItemBinding>inflateInternal(inflater, R.layout.song_item, null, false, component);
  }

  public static SongItemBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static SongItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (SongItemBinding)bind(component, view, R.layout.song_item);
  }
}
