package activatecontract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.brothersgas.DashBoard;
import com.brothersgas.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.ContractModel;


/**
 * Created by ashish.kumar on 03-04-2019.
 */

public class Dashboard2  extends Activity implements View.OnClickListener{
    String owner;
    String project;
    @BindView(R.id.activatecontract)
    View activatecontract;
    @BindView(R.id.newContract)
    View newcontract;
    @BindView(R.id.back_button)
    Button back_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard2);
        ButterKnife.bind(this);


        owner = getIntent().getStringExtra("owner");
        project = getIntent().getStringExtra("project");
        activatecontract.setOnClickListener(this);
        back_button.setOnClickListener(this);
        newcontract.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.back_button:
              finish();
              break;
          case R.id.newContract:
              Intent in=new Intent(Dashboard2.this,NewContractList.class);
              in.putExtra("owner",owner);
              in.putExtra("project",project);
              startActivity(in);

              break;
          case R.id.activatecontract:
              in=new Intent(Dashboard2.this,ContractListForActivation.class);
              in.putExtra("owner",owner);
              in.putExtra("project",project);
              startActivity(in);

              break;
      }
    }
}
