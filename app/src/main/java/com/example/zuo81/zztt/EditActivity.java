package com.example.zuo81.zztt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.zuo81.zztt.utils.ConstantHelper.PUT_EXTRA_NAME_EDIT_ACTIVITY;
import static com.example.zuo81.zztt.utils.ConstantHelper.RESULT_CODE_GET_EDIT_NAME;

public class EditActivity extends AppCompatActivity {
    EditText mEditTextChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mEditTextChangeName = (EditText)findViewById(R.id.edit_text_for_change_name);
        Button mButtonOK = (Button)findViewById(R.id.button_for_change_name);

        toolbar.setTitle("姓名");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mEditTextChangeName.getText().toString();
                if (!s.equals("")) {
                    Intent data = new Intent();
                    data.putExtra(PUT_EXTRA_NAME_EDIT_ACTIVITY, s);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(view.getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
