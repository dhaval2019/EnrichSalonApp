package com.enrich.salonapp.ui.activities;

import android.Manifest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AddressResponseModel;
import com.enrich.salonapp.data.model.FriendResponseModel;
import com.enrich.salonapp.data.model.GuestMobileNo;
import com.enrich.salonapp.data.model.GuestModel;
import com.enrich.salonapp.data.model.ReferFriendModel;
import com.enrich.salonapp.data.model.SelectFriendModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.adapters.SelectFriendAdapter;
import com.enrich.salonapp.ui.contracts.AddressContract;
import com.enrich.salonapp.ui.contracts.FriendContract;
import com.enrich.salonapp.ui.presenters.AddressPresenter;
import com.enrich.salonapp.ui.presenters.FriendPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.mvp.BaseActivity;
import com.enrich.salonapp.util.supertoast.utils.HelloService;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFriendActivity extends BaseActivity implements FriendContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.continue_button)
    Button btnContinue;//by dhaval shah 7/7/19

   // public static Button btnRefresh;//by dhaval shah 30/7/19
    private static final int PERMISSION_REQUEST_CONTACT = 1;
    private   SelectFriendAdapter adapter;
    //public static ArrayList<SelectFriendModel> albumList = new ArrayList<>();
    public static ArrayList<SelectFriendModel> searchList = new ArrayList<>();
    public static ArrayList<SelectFriendModel> selectedList = new ArrayList<>();
    @BindView(R.id.searchbar)
    EditText serachBar;
    private FriendPresenter friendPresenter;
    private DataRepository dataRepository;
    @BindView(R.id.tvclose)
    TextView tvClose;
    int index = 0;
    int size = 10;
    int offset = 10;
    private Cursor cursor;
    ContentResolver contentResolver;
    //public static int isFirstTime = 0;

    //
    Tracker mTracker;
    EnrichApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
        ButterKnife.bind(this);
       // btnRefresh = (Button) findViewById(R.id.refresh_button);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        application = (EnrichApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Select friend screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);
        dataRepository = Injection.provideDataRepository(this, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        friendPresenter = new FriendPresenter(this, dataRepository);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectFriendActivity.this, ReferAFriendActivity.class);
                startActivity(intent);
                SelectFriendActivity.this.finish();


            }
        });
       /* btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              *//*  searchList.clear();
                albumList.clear();
                index = 0;
                size = 10;
                offset = 10;
                new LoadContacts().execute();*//*


            }
        });*/

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("list-updated"));

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedList.clear();
                if (searchList.isEmpty()) {// I am putting this here since we must take latest selected contacts
                    for (int i = 0; i < SplashActivity.albumList.size(); i++) {
                        if ( SplashActivity.albumList.get(i).getIsSelect()) {
                            selectedList.add( SplashActivity.albumList.get(i));
                        } else {

                        }
                    }
                } else {
                    for (int j = 0; j < SplashActivity.albumList.size(); j++) {
                        if ( SplashActivity.albumList.get(j).getIsSelect()) {
                            selectedList.add( SplashActivity.albumList.get(j));
                        }


                    }
                    for (int m = 0; m < searchList.size(); m++) {
                        if (searchList.get(m).getIsSelect()) {
                            if (selectedList.contains(searchList.get(m))) {

                            } else {
                                selectedList.add(searchList.get(m));
                            }
                        }
                    }
                }
                ArrayList<GuestMobileNo> guestReferredMobileNos = new ArrayList<GuestMobileNo>();
                ReferFriendModel referFriendModel = new ReferFriendModel();
                if(EnrichUtils.getUserData(SelectFriendActivity.this)!=null) {
                    referFriendModel.setGuestId(EnrichUtils.getUserData(SelectFriendActivity.this).Id);
                }
                for (int j = 0; j < selectedList.size(); j++) {
                    GuestMobileNo guestMobileNo = new GuestMobileNo();
                    guestMobileNo.setMobileNo(selectedList.get(j).getMobNo());
                    guestReferredMobileNos.add(guestMobileNo);
                    // Log.e("name", selectedList.get(j).getName());
                }
                referFriendModel.setGuestReferredMobileNos(guestReferredMobileNos);
                friendPresenter.referFriend(SelectFriendActivity.this, referFriendModel);


            }
        });
        searchList.clear();
       /* if (isFirstTime == 0) {
            askForContactPermission();
            isFirstTime = 1;
        } else {*/
            for(int m=0;m< SplashActivity.albumList.size();m++)
            {
                SplashActivity.albumList.get(m).setIsSelect(false);
            }
            adapter = new SelectFriendAdapter(SelectFriendActivity.this,  SplashActivity.albumList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(SelectFriendActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);
     //   }
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        cardView.setAnimation(animation);
        serachBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                searchList.clear();
                for (int i = 0; i <  SplashActivity.albumList.size(); i++) {
                    if ( SplashActivity.albumList.get(i).getName().toLowerCase().contains(cs.toString().toLowerCase())) {
                        searchList.add( SplashActivity.albumList.get(i));

                    }
                }
                adapter = new SelectFriendAdapter(SelectFriendActivity.this, searchList);

                LinearLayoutManager layoutManager = new LinearLayoutManager(SelectFriendActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void friendReferred(FriendResponseModel model) {
        if (model != null) {
            // Log.e("tag1",model.ExistingGuests.get(0).getMobileNo());
            //  Log.e("tag2",model.toString());
            // Log.e("tag2",model.ExistingReferrals.get(0).getMobileNo());
            Intent intent = new Intent(SelectFriendActivity.this, ThankyouActivity.class);
            intent.putExtra("friendResponseModel", model);
            startActivity(intent);
            SelectFriendActivity.this.finish();
        }
    }
   private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if(message.equalsIgnoreCase("done"))
            {
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SelectFriendActivity.this, ReferAFriendActivity.class);
        startActivity(intent);
        SelectFriendActivity.this.finish();
    }

    /*public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                searchList.clear();
                HelloService.albumList.clear();


                new LoadContacts().execute();


            }
        } else {
            searchList.clear();
            HelloService.albumList.clear();

            new LoadContacts().execute();

            *//*adapter = new SelectFriendAdapter(this, albumList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);*//*
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchList.clear();
                    albumList.clear();


                    new LoadContacts().execute();

                    *//*adapter = new SelectFriendAdapter(this, albumList);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(layoutManager);

                    recyclerView.setAdapter(adapter);*//*
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_LONG).show();
                    SelectFriendActivity.this.finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public  ArrayList<SelectFriendModel> removeDuplicates(ArrayList<SelectFriendModel> param1)
    {

        LinkedHashSet<SelectFriendModel> lhs = new LinkedHashSet<SelectFriendModel>();

        *//* Adding ArrayList elements to the LinkedHashSet
         * in order to remove the duplicate elements and
         * to preserve the insertion order.
         *//*
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
            contentResolver = SelectFriendActivity.this.getContentResolver();
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            size = cursor.getCount();
            if (cursor.getCount() > 0) {
                cursor.moveToPosition(index * offset);
                while ((cursor.moveToNext()) && (cursor.getPosition() <= ((index + 1) * offset))) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(SelectFriendActivity.this.getContentResolver(),
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

                            String mobNo=cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            mobNo=    mobNo.replaceAll("-", "");
                            mobNo=    mobNo.replaceAll("\\s+", "");
                            mobNo=  mobNo.replaceAll(" ", "");
                            info.setMobileNo(mobNo);

                            info.setPhoto(pURI);
                            if (!albumList.contains(info)) {
                                albumList.add(info);
                            }

                        }

                        cursorInfo.close();

                    }
                }
            }

            cursor.close();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            albumList=removeDuplicates(albumList);
            *//* if (index == 0) {*//*
            adapter = new SelectFriendAdapter(SelectFriendActivity.this, albumList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(SelectFriendActivity.this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);
           *//* } else {
                adapter.notifyDataSetChanged();
            }*//*

          *//*  if (pd.isShowing())
                pd.dismiss();*//*
            index = index + 1;
            if (index * offset <= size) {
                new LoadContacts().execute();
            }


        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
           *//* pd = ProgressDialog.show(SelectFriendActivity.this, "Loading Contacts",
                    "Please Wait...");*//*
        }

    }*/


}
