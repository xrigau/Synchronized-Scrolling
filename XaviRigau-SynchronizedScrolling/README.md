Synchronized Scrolling Library
==============================

Synchronized Scrolling Library is a library that allows you to have a Synchronized scrolling effect in a view, similar to the one in the GMail app (when you open a conversation with multiple messages, you can scroll down and the senders info stay on top of screen), or in the Play Store App (when you want to download an app and it shows the permision screen). For now, this library only works for Android 2.2 or newer (Tested on 2.2, 2.3 and 4.0, I suppose that will work in Honeycomb).

You can see that effect here:

![Example Image - Play Store App 1][1]	 - 	![Example Image - Play Sore App 2][2]


Introduction
------------

You can find more info on synchronized scrolling at [Kirill Grouchnikov's blog][3].
This is an example of what you'll be able to create with this library:

![Example Gmail style][4]


Usage
-----

To use this library, follow this steps:

* First of all, download at least the library project and add it to your project as an Android library project.

* Then you should declare an EnhancedScrollView in your layout. It's equivalent to declaring a ScrollView:

<com.xrigau.syncscrolling.view.EnhancedScrollView
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:fadingEdge="none" >


* An EnhancedScrollView can contain one or more SynchronizedRelativeLayouts (if you want to have more than one, you can put them inside a Layout, because as an ScrollView, an EnhancedScrollView can only host one direct child. You must put a SynchronizedRelativeLayout inside your EnhancedScrollView:

        <com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            xrigau:placeholderView="@+id/base_view"
            xrigau:synchronizedView="@+id/synchronized_view" >

* Your SynchronizedRelativeLayout must contain at least 2 Views, the one that will have the synchronized scroll behaviour (in this case its id is @+id/synchronized_view) and a dummy placeholder (@+id/base_view) that defines the position of the synchronized view (when it's not at screen's top). The SynchronizedRelativeLayout sets the dummy view's size to the same as the sync view's, so don't worry about that.

            <View
                android:id="@+id/top"
                android:layout_width="fill_parent"
                android:layout_height="100dip"
                android:layout_alignParentTop="true"
                android:background="#00ff00" />

            <View
                android:id="@+id/base_view"
                android:layout_width="fill_parent"
                android:layout_height="0dp"
                android:layout_below="@+id/top" />

            <View android:id="@+id/last_view"
                android:layout_width="fill_parent"
                android:layout_height="1000dip"
                android:layout_below="@+id/base_view"
                android:background="#00ff00" />

            <View
                android:id="@+id/synchronized_view"
                android:layout_width="fill_parent"
                android:layout_height="100dip"
                android:background="#ff0000" />

In this case we have 4 views, the @+id/top, will be on top, the @+id/base_view is our placeholder, that defines that the sync view will stay below the top view; then we have the @+id/last_view (this goes below the placeholder, just to fill the screen and provide enough splace for scrolling). And at last we declared the @+id/synchronized_view, this is the sync view that will be put where the placehoder is (below the top view). We just declare the sync view at last so it's drawn over everything.

* Putting all together. For the basic example you just need to add this to your main.xml layout:

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:xrigau="http://schemas.android.com/apk/res-auto"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <com.xrigau.syncscrolling.view.EnhancedScrollView
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:fadingEdge="none" >

        <com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            xrigau:placeholderView="@+id/base_view"
            xrigau:synchronizedView="@+id/synchronized_view" >

            <View
                android:id="@+id/top"
                android:layout_width="fill_parent"
                android:layout_height="100dip"
                android:layout_alignParentTop="true"
                android:background="#00ff00" />

            <View
                android:id="@+id/base_view"
                android:layout_width="fill_parent"
                android:layout_height="0dp"
                android:layout_below="@+id/top" />

            <View
                android:layout_width="fill_parent"
                android:layout_height="1000dip"
                android:layout_below="@+id/base_view"
                android:background="#00ff00" />

            <View
                android:id="@+id/synchronized_view"
                android:layout_width="fill_parent"
                android:layout_height="100dip"
                android:background="#ff0000" />
        </com.xrigau.syncscrolling.view.SynchronizedRelativeLayout>
    </com.xrigau.syncscrolling.view.EnhancedScrollView>

</LinearLayout>


And that's it! No Java code! Enjoy!

Developed by
------------

* Xavi Rigau - <xrigau@gmail.com>



License
-------

    Copyright 2012 Xavi Rigau

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

	
 [1]: http://farm7.static.flickr.com/6002/5951568960_83645dd316.jpg
 [2]: http://farm7.static.flickr.com/6137/5951013387_4996df9339.jpg
 [3]: http://www.pushing-pixels.org/2011/07/18/android-tips-and-tricks-synchronized-scrolling.html
 [4]: http://xavirigau.com/public_img/github_intro_gmail_style_example.jpg