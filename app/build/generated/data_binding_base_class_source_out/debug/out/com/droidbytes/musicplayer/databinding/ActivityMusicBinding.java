// Generated by data binding compiler. Do not edit!
package com.droidbytes.musicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.droidbytes.musicplayer.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMusicBinding extends ViewDataBinding {
  @NonNull
  public final ImageView playPauseButton;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final SeekBar seekbar;

  @NonNull
  public final TextView time;

  protected ActivityMusicBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView playPauseButton, ProgressBar progressBar, SeekBar seekbar, TextView time) {
    super(_bindingComponent, _root, _localFieldCount);
    this.playPauseButton = playPauseButton;
    this.progressBar = progressBar;
    this.seekbar = seekbar;
    this.time = time;
  }

  @NonNull
  public static ActivityMusicBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_music, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMusicBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMusicBinding>inflateInternal(inflater, R.layout.activity_music, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMusicBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_music, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMusicBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMusicBinding>inflateInternal(inflater, R.layout.activity_music, null, false, component);
  }

  public static ActivityMusicBinding bind(@NonNull View view) {
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
  public static ActivityMusicBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMusicBinding)bind(component, view, R.layout.activity_music);
  }
}