package br.edu.utfpr.dv.siacoes.ui.views;

import java.util.List;
import java.util.logging.Level;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.edu.utfpr.dv.siacoes.Session;
import br.edu.utfpr.dv.siacoes.bo.FinalSubmissionBO;
import br.edu.utfpr.dv.siacoes.log.Logger;
import br.edu.utfpr.dv.siacoes.model.FinalSubmission;
import br.edu.utfpr.dv.siacoes.model.Module.SystemModule;
import br.edu.utfpr.dv.siacoes.model.User.UserProfile;
import br.edu.utfpr.dv.siacoes.ui.MainLayout;
import br.edu.utfpr.dv.siacoes.ui.grid.FinalSubmissionDataSource;

@PageTitle("Acadêmicos Aprovados")
@Route(value = "finalsubmission", layout = MainLayout.class)
public class FinalSubmissionView extends ListView<FinalSubmissionDataSource> {
	
	private final Button buttonFinalReport;
	
	public FinalSubmissionView() {
		super(SystemModule.SIGAC);
		this.setProfilePerimissions(UserProfile.MANAGER);
		
		this.getGrid().addColumn(FinalSubmissionDataSource::getStudent, "Student").setHeader("Acadêmico");
		this.getGrid().addColumn(FinalSubmissionDataSource::getFinalScore).setHeader("Pont. Final").setFlexGrow(0).setWidth("150px");
		this.getGrid().addColumn(new LocalDateRenderer<>(FinalSubmissionDataSource::getDate, "dd/MM/yyyy"), "Date").setHeader("Data").setFlexGrow(0).setWidth("150px");
		this.getGrid().addColumn(FinalSubmissionDataSource::getFeedbackUser, "FeedbackUser").setHeader("Avaliador");
		
		this.buttonFinalReport = new Button("Relatório Final", new Icon(VaadinIcon.FILE_TEXT_O), event -> {
            finalReport();
        });
		
		this.setFiltersVisible(false);
		this.setAddVisible(false);
		this.setEditVisible(false);
		this.setDeleteVisible(false);
		this.addActionButton(this.buttonFinalReport);
	}

	@Override
	protected void loadGrid() {
		try{
			FinalSubmissionBO bo = new FinalSubmissionBO();
			List<FinalSubmission> list = bo.listByDepartment(Session.getSelectedDepartment().getDepartment().getIdDepartment());
			
			this.getGrid().setItems(FinalSubmissionDataSource.load(list));
		}catch(Exception e){
			Logger.log(Level.SEVERE, e.getMessage(), e);
			
			this.showErrorNotification("Listar Acadêmicos", e.getMessage());
		}
	}
	
	private void finalReport() {
		Object value = this.getIdSelected();
    	
    	if(value == null){
    		this.showWarningNotification("Relatório Final", "Selecione o acadêmico para imprimir o relatório final.");
    	}else{
    		try {
    			FinalSubmissionBO bo = new FinalSubmissionBO();
				FinalSubmission submission = bo.findById((int)value);
				
				this.showReport(submission.getReport());
			} catch (Exception e) {
				e.printStackTrace();
				
				this.showErrorNotification("Relatório Final", e.getMessage());
			}
    	}
	}

	@Override
	public void addClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editClick(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteClick(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filterClick() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
