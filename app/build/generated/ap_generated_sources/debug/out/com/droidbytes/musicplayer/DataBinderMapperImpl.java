package com.droidbytes.musicplayer;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.droidbytes.musicplayer.databinding.ActivityExternalAudioFileBindingImpl;
import com.droidbytes.musicplayer.databinding.ActivityMainBindingImpl;
import com.droidbytes.musicplayer.databinding.ActivityMusicBindingImpl;
import com.droidbytes.musicplayer.databinding.ActivitySongsListBindingImpl;
import com.droidbytes.musicplayer.databinding.FragmentNowPlayingMusicBindingImpl;
import com.droidbytes.musicplayer.databinding.SongItemBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYEXTERNALAUDIOFILE = 1;

  private static final int LAYOUT_ACTIVITYMAIN = 2;

  private static final int LAYOUT_ACTIVITYMUSIC = 3;

  private static final int LAYOUT_ACTIVITYSONGSLIST = 4;

  private static final int LAYOUT_FRAGMENTNOWPLAYINGMUSIC = 5;

  private static final int LAYOUT_SONGITEM = 6;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(6);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.activity_external_audio_file, LAYOUT_ACTIVITYEXTERNALAUDIOFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.activity_music, LAYOUT_ACTIVITYMUSIC);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.activity_songs_list, LAYOUT_ACTIVITYSONGSLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.fragment_now_playing_music, LAYOUT_FRAGMENTNOWPLAYINGMUSIC);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.droidbytes.musicplayer.R.layout.song_item, LAYOUT_SONGITEM);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYEXTERNALAUDIOFILE: {
          if ("layout/activity_external_audio_file_0".equals(tag)) {
            return new ActivityExternalAudioFileBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_external_audio_file is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMUSIC: {
          if ("layout/activity_music_0".equals(tag)) {
            return new ActivityMusicBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_music is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSONGSLIST: {
          if ("layout/activity_songs_list_0".equals(tag)) {
            return new ActivitySongsListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_songs_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNOWPLAYINGMUSIC: {
          if ("layout/fragment_now_playing_music_0".equals(tag)) {
            return new FragmentNowPlayingMusicBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_now_playing_music is invalid. Received: " + tag);
        }
        case  LAYOUT_SONGITEM: {
          if ("layout/song_item_0".equals(tag)) {
            return new SongItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for song_item is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(6);

    static {
      sKeys.put("layout/activity_external_audio_file_0", com.droidbytes.musicplayer.R.layout.activity_external_audio_file);
      sKeys.put("layout/activity_main_0", com.droidbytes.musicplayer.R.layout.activity_main);
      sKeys.put("layout/activity_music_0", com.droidbytes.musicplayer.R.layout.activity_music);
      sKeys.put("layout/activity_songs_list_0", com.droidbytes.musicplayer.R.layout.activity_songs_list);
      sKeys.put("layout/fragment_now_playing_music_0", com.droidbytes.musicplayer.R.layout.fragment_now_playing_music);
      sKeys.put("layout/song_item_0", com.droidbytes.musicplayer.R.layout.song_item);
    }
  }
}
