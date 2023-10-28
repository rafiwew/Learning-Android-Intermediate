package com.piwew.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackStoriesWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(i: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}