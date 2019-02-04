package azuka.com.cataloguemovie;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Ivana Situmorang on 2/3/2019.
 */
public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
