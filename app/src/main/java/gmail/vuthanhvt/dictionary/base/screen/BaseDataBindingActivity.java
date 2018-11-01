package gmail.vuthanhvt.dictionary.base.screen;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import gmail.vuthanhvt.dictionary.BR;

/**
 * Create by FRAMGIA\vu.anh.thanh on 22/10/2018.
 * Phone: 096.320.8650
 * Email: vuthanhvt@gmail.com
 * <p>
 * Class BaseDataBindingActivity.
 */
public abstract class BaseDataBindingActivity<V extends ViewDataBinding, P extends BasePresenter>
        extends BaseActivity {

    protected V mBinding;

    protected P mPresenter;

    public abstract int getContentViewLayoutId();

    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getContentViewLayoutId());
        initData();
        mBinding.setVariable(BR.presenter, mPresenter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public boolean enableTransaction() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }
}
