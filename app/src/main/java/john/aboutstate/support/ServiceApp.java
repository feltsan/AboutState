package john.aboutstate.support;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by john on 03.12.14.
 */
public class ServiceApp extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
