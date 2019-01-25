package interfaces;

import model.ContractModel;

/**
 * Created by ashish.kumar on 25-01-2019.
 */

public interface ListItemClickListner {
    public void onClick(ContractModel model);
    public void onCancelClick(ContractModel model);
    public void onBlockClick(ContractModel model);
}
