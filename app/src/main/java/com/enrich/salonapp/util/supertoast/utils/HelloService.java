package com.enrich.salonapp.util.supertoast.utils;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.ui.activities.SelectFriendActivity;
import com.enrich.salonapp.ui.activities.SplashActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class HelloService extends Service {
    private static final int PERMISSION_REQUEST_CONTACT = 1;
   // public static ArrayList<SelectFriendModel> albumList = new ArrayList<>();
    int index = 0;
    int size = 10;
    int offset = 10;
    private Cursor cursor;
    ContentResolver contentResolver;
    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;

    /**
     * interface for clients that bind
     */
    IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {

    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SplashActivity.albumList.clear();
        new LoadContacts().execute();

        return mStartMode;
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {

    }

    public ArrayList<SelectFriendModel> removeDuplicates(ArrayList<SelectFriendModel> param1) {

        LinkedHashSet<SelectFriendModel> lhs = new LinkedHashSet<SelectFriendModel>();

        /* Adding ArrayList elements to the LinkedHashSet
         * in order to remove the duplicate elements and
         * to preserve the insertion order.
         */
        lhs.addAll(param1);

        // Removing ArrayList elements
        param1.clear();

        // Adding LinkedHashSet elements to the ArrayList
        param1.addAll(lhs);
        return param1;
    }

    private class LoadContacts extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pd;


        @Override
        protected Void doInBackground(Void... params) {
            contentResolver = getBaseContext().getContentResolver();
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if(cursor!=null) {
                size = cursor.getCount();
                if (cursor.getCount() > 0) {
                    cursor.moveToPosition(index * offset);
                    while ((cursor.moveToNext()) && (cursor.getPosition() <= ((index + 1) * offset))) {

                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getBaseContext().getContentResolver(),
                                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                            Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                            Bitmap photo = null;
                            if (inputStream != null) {
                                photo = BitmapFactory.decodeStream(inputStream);
                            }
                            while (cursorInfo.moveToNext()) {
                                SelectFriendModel info = new SelectFriendModel();
                                info.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                                String mobNo = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                try {
                                    mobNo = mobNo.replaceAll("-", "");
                                } catch (Exception e) {

                                }
                                mobNo = mobNo.replaceAll("\\s+", "");
                                mobNo = mobNo.replaceAll(" ", "");
                                info.setMobileNo(mobNo);

                                info.setPhoto(pURI);
                                if (!SplashActivity.albumList.contains(info)) {
                                    SplashActivity.albumList.add(info);
                                }

                            }

                            cursorInfo.close();

                        }
                    }
                }

                cursor.close();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            SplashActivity.albumList = removeDuplicates(SplashActivity.albumList);
            Intent intent = new Intent("list-updated");
            // You can also include some extra data.
            intent.putExtra("message", "done");
            LocalBroadcastManager.getInstance(HelloService.this).sendBroadcast(intent);
//            SelectFriendActivity.adapter.notifyDataSetChanged();
            //Log.e("name", SplashActivity.albumList.get(index).getName());
            /* if (index == 0) {*/

           /* } else {
                adapter.notifyDataSetChanged();
            }*/

          /*  if (pd.isShowing())
                pd.dismiss();*/
            index = index + 1;
            if (index * offset <= size) {
                new LoadContacts().execute();
            } else {
                stopSelf();
            }


        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
           /* pd = ProgressDialog.show(SelectFriendActivity.this, "Loading Contacts",
                    "Please Wait...");*/
        }

    }
}
