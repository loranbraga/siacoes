package br.edu.utfpr.dv.siacoes.ui.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.edu.utfpr.dv.siacoes.Session;
import br.edu.utfpr.dv.siacoes.bo.ActivitySubmissionBO;
import br.edu.utfpr.dv.siacoes.model.Module.SystemModule;
import br.edu.utfpr.dv.siacoes.model.StudentActivityStatusReport.StudentStage;
import br.edu.utfpr.dv.siacoes.model.User.UserProfile;
import br.edu.utfpr.dv.siacoes.ui.MainLayout;

@PageTitle("Relatório de Situação do Acadêmico em Atividades Complementares")
@Route(value = "studentactivitystatusreport", layout = MainLayout.class)
public class StudentActivityStatusReportView extends ReportView {
	
	private final Select<StudentStage> comboStage;
	private final Checkbox checkStudentsWithoutPoints;

	public StudentActivityStatusReportView(){
		super(SystemModule.SIGAC);
		this.setProfilePerimissions(UserProfile.MANAGER);
		
		this.comboStage = new Select<StudentStage>();
		this.comboStage.setLabel("Situação do Acadêmico");
		this.comboStage.setWidth("400px");
		this.comboStage.setItems(StudentStage.REGULAR, StudentStage.FINISHINGCOURSE, StudentStage.ALMOSTGRADUATED);
		//this.comboStage.addItem(StudentStage.GRADUATED);
		this.comboStage.setValue(StudentStage.REGULAR);
		
		this.checkStudentsWithoutPoints = new Checkbox("Filtrar apenas acadêmicos que ainda não atingiram a pontuação necessária");
		
		this.addFilterField(this.comboStage);
		this.addFilterField(this.checkStudentsWithoutPoints);
	}
	
	@Override
	public byte[] generateReport() throws Exception {
		ActivitySubmissionBO bo = new ActivitySubmissionBO();
		return bo.getStudentActivityStatusReport(Session.getSelectedDepartment().getDepartment().getIdDepartment(), (StudentStage)this.comboStage.getValue(), this.checkStudentsWithoutPoints.getValue());
	}

}
