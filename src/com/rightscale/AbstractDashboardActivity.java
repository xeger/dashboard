package com.rightscale;

import net.xeger.rest.RestException;
import net.xeger.rest.ui.ContentConsumer;
import net.xeger.rest.ui.ContentProducer;
import net.xeger.rest.ui.ContentTransfer;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class AbstractDashboardActivity extends ListActivity implements ContentProducer, ContentConsumer {
	private static String HARDCODED_ACCOUNT_ID = "2951"; // 2951 = DEMO

	protected Helper            _helper   = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	if(getIntent().getData() != null) {
    		_helper = new Helper(this, Routes.getAccountId(getIntent().getData()));
    	}
    	else {
    		//HACK if someone launches us without a data-ful intent (really we should hit up a dashboard here!)
    		_helper = new Helper(this, HARDCODED_ACCOUNT_ID);
    	}
        ContentTransfer.load(this, this, new Handler());        
    }

    @Override
    public void onStart() {
    	super.onStart();
    	_helper.onStart();
    }

    @Override
    public void onResume() {
    	super.onResume();
    	_helper.onResume();
    }

    @Override
    public void onPause() {
    	super.onPause();
    	_helper.onPause();
    }

    @Override
    public void onStop() {
    	super.onStop();
    	_helper.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i = null;
    	
    	switch(item.getItemId()) {
    	case R.id.menu_deployments:
        	i = new Intent(Intent.ACTION_VIEW, Routes.indexDeployments(_helper.getAccountId()));
        	break;
    	case R.id.menu_settings:
    		i = new Intent(this, Settings.class);
    		break;
    	}
    	
    	if(i != null) {
    		startActivity(i);
    		return true;
    	}
    	else {
    		return false;
    	}
    }

	abstract public Cursor produceContent(String tag) throws RestException;
	abstract public void consumeContent(Cursor c, String tag);

	public void consumeContentError(Throwable t, String tag) {
		Settings.handleError(t, this);
		finish();
	}	
}
