package gmail.vuthanhvt.dictionary.screen.mongviet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gmail.vuthanhvt.dictionary.R;
import gmail.vuthanhvt.dictionary.screen.addnew.NewWordActivity;
import gmail.vuthanhvt.dictionary.screen.mongviet.view.EditWordDialogFragment;
import gmail.vuthanhvt.dictionary.base.screen.BaseDataBindingActivity;
import gmail.vuthanhvt.dictionary.base.widgets.SmoothLayoutManager;
import gmail.vuthanhvt.dictionary.data.DataBaseHelper;
import gmail.vuthanhvt.dictionary.data.model.MongViet;
import gmail.vuthanhvt.dictionary.databinding.ActivityMongvietBinding;
import gmail.vuthanhvt.dictionary.screen.mongviet.view.MongVietAdapter;

/**
 * Create by FRAMGIA\vu.anh.thanh on 26/10/2018.
 * Phone: 096.320.8650
 * Email: vuthanhvt@gmail.com
 * <p>
 * Class MongVietActivity.
 */
public class MongVietActivity extends BaseDataBindingActivity<ActivityMongvietBinding, MongVietPresenter>
        implements MongVietView, MongVietAdapter.OnLongClickListener,
            EditWordDialogFragment.onDialogPositiveClick {

    public static final String TAG = "VietMongActivity";

    //private LoadingDialog mDialog;

    private DataBaseHelper mDBHelper;

    private List<MongViet> mList;

    private MongVietAdapter mAdapter;

    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_mongviet;
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mDBHelper = new DataBaseHelper(this);
        mPresenter = new MongVietPresenter(this);
        mAdapter = new MongVietAdapter(this);
        mAdapter.setLongClickListener(this);
        mBinding.listWords.setLayoutManager(new SmoothLayoutManager(context()));
        mBinding.setAdapter(mAdapter);
        //showLoading();
        mPresenter.getData();
        mBinding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mDBHelper.createDatabase();
                    mDBHelper.openDatabase();
                    mList.clear();
                    mDBHelper.getAllTableMongViet(mList,
                            mBinding.searchEdt.getText().toString(), "TuMong");
                    if (mList.size() == 0) {
                        return;
                    }
                    mAdapter.refreshDataView(mList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showLoading() {
        /*if (mDialog == null || mDialog.isShowing()) {
            return;
        }
        mDialog = new LoadingDialog(this, true);
        if (!isFinishing()) {
            mDialog.show();
        }*/
    }

    @Override
    public void hideLoading() {
        /*if (mDialog == null || !mDialog.isShowing()) {
            return;
        }
        mDialog.dismiss();*/
    }

    @Override
    public void showError() {

    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoading();
    }

    @Override
    public void onClickBackView() {
        onBackPressed();
    }

    @Override
    public void loadListWords() {
        Log.d(TAG, "loadListWords: ");
        try {
            mDBHelper.createDatabase();
            mDBHelper.openDatabase();
            mDBHelper.getAllTableMongViet(mList, "", "TuMong");
            if (mList.size() == 0) {
                return;
            }
            mAdapter.refreshDataView(mList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickAddView() {
        Intent intent = new Intent(this, NewWordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLongClick(MongViet item) {
        Log.d(TAG, "onLongClick: ");
        FragmentManager fm = getSupportFragmentManager();
        Bundle data = new Bundle();
        data.putSerializable("ITEM_CHOSEN", item);
        EditWordDialogFragment dialog = EditWordDialogFragment.newInstance(data);
        dialog.setListener(this);
        dialog.show(fm, EditWordDialogFragment.TAG);
    }

    @Override
    public void onDialogPositiveClick(MongViet item) {
        Log.d(TAG, "onDialogPositiveClick: ");
        try {
            mDBHelper.createDatabase();
            mDBHelper.openDatabase();
            mList.clear();
            mDBHelper.updateRowDictionary(item.getVietnameseWord(),
                    item.getHMongWord(),
                    item.getReading(),
                    true);
            mDBHelper.getAllTableMongViet(mList, "", "NghiaViet");
            if (mList.size() == 0) {
                return;
            }
            mAdapter.refreshDataView(mList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}