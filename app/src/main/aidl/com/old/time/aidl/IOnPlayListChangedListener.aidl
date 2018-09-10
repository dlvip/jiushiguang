// IOnSongChange.aidl
package com.old.time.aidl;
import com.old.time.aidl.Song;

// Declare any non-default types here with import statements

interface IOnPlayListChangedListener {

    void onPlayListChange(in Song current,int index,int id);
}
