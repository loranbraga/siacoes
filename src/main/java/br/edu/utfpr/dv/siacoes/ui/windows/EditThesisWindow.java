package br.edu.utfpr.dv.siacoes.ui.windows;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import br.edu.utfpr.dv.siacoes.Session;
import br.edu.utfpr.dv.siacoes.bo.CampusBO;
import br.edu.utfpr.dv.siacoes.bo.DeadlineBO;
import br.edu.utfpr.dv.siacoes.bo.ProjectBO;
import br.edu.utfpr.dv.siacoes.bo.ProposalBO;
import br.edu.utfpr.dv.siacoes.bo.SemesterBO;
import br.edu.utfpr.dv.siacoes.bo.SigetConfigBO;
import br.edu.utfpr.dv.siacoes.bo.SupervisorChangeBO;
import br.edu.utfpr.dv.siacoes.bo.ThesisBO;
import br.edu.utfpr.dv.siacoes.log.Logger;
import br.edu.utfpr.dv.siacoes.model.Campus;
import br.edu.utfpr.dv.siacoes.model.Deadline;
import br.edu.utfpr.dv.siacoes.model.Project;
import br.edu.utfpr.dv.siacoes.model.Proposal;
import br.edu.utfpr.dv.siacoes.model.Semester;
import br.edu.utfpr.dv.siacoes.model.SigetConfig;
import br.edu.utfpr.dv.siacoes.model.Thesis;
import br.edu.utfpr.dv.siacoes.ui.components.CampusComboBox;
import br.edu.utfpr.dv.siacoes.ui.components.DepartmentComboBox;
import br.edu.utfpr.dv.siacoes.ui.components.FileUploader;
import br.edu.utfpr.dv.siacoes.ui.components.FileUploaderListener;
import br.edu.utfpr.dv.siacoes.ui.components.SemesterComboBox;
import br.edu.utfpr.dv.siacoes.ui.components.SupervisorComboBox;
import br.edu.utfpr.dv.siacoes.ui.components.YearField;
import br.edu.utfpr.dv.siacoes.ui.components.FileUploader.AcceptedDocumentType;
import br.edu.utfpr.dv.siacoes.ui.views.ListView;
import br.edu.utfpr.dv.siacoes.util.DateUtils;

public class EditThesisWindow extends EditWindow {

	private final Thesis thesis;
	
	private final CampusComboBox comboCampus;
	private final DepartmentComboBox comboDepartment;
	private final TextField textTitle;
	private final TextField textSubarea;
	private final TextField textStudent;
	private final SupervisorComboBox comboSupervisor;
	private final SupervisorComboBox comboCosupervisor;
	private final SemesterComboBox comboSemester;
	private final YearField textYear;
	private final DatePicker textSubmissionDate;
	private final FileUploader uploadFile;
	private final Button buttonDownloadFile;
	private final TextArea textAbstract;
	private final Tabs tabData;
	
	private SigetConfig config;
	
	public EditThesisWindow(Thesis t, ListView parentView){
		super("Editar Monografia", parentView);
		
		if(t == null){
			this.thesis = new Thesis();
		}else{
			this.thesis = t;
		}
		
		try {
			if(this.thesis.getIdThesis() > 0) {
				this.config = new SigetConfigBO().findByDepartment(new ThesisBO().findIdDepartment(this.thesis.getIdThesis()));
			} else {
				this.config = new SigetConfigBO().findByDepartment(new ProjectBO().findIdDepartment(this.thesis.getProject().getIdProject()));
			}
		} catch (Exception e1) {
			this.config = new SigetConfig();
		}
		
		this.comboCampus = new CampusComboBox();
		this.comboCampus.setEnabled(false);
		
		this.comboDepartment = new DepartmentComboBox(0);
		this.comboDepartment.setEnabled(false);
		
		this.textTitle = new TextField("Título");
		this.textTitle.setWidth("400px");
		this.textTitle.setMaxLength(255);
		this.textTitle.setRequired(true);
		
		this.textSubarea = new TextField("Subárea");
		this.textSubarea.setWidth("400px");
		this.textSubarea.setMaxLength(255);
		this.textSubarea.setRequired(true);
		
		this.textStudent = new TextField("Acadêmico");
		this.textStudent.setEnabled(false);
		this.textStudent.setWidth("800px");
		this.textStudent.setRequired(true);
		
		this.comboSupervisor = new SupervisorComboBox("Orientador", Session.getSelectedDepartment().getDepartment().getIdDepartment(), new SigetConfigBO().getSupervisorFilter(Session.getSelectedDepartment().getDepartment().getIdDepartment()));
		this.comboSupervisor.setEnabled(false);
		this.comboSupervisor.setRequired(true);
		
		this.comboCosupervisor = new SupervisorComboBox("Coorientador", Session.getSelectedDepartment().getDepartment().getIdDepartment(), new SigetConfigBO().getCosupervisorFilter(Session.getSelectedDepartment().getDepartment().getIdDepartment()));
		this.comboCosupervisor.setEnabled(false);
		
		this.comboSemester = new SemesterComboBox();
		this.comboSemester.setEnabled(false);
		
		this.textYear = new YearField();
		this.textYear.setEnabled(false);
		
		this.textSubmissionDate = new DatePicker("Data de Submissão");
		this.textSubmissionDate.setEnabled(false);
		this.textSubmissionDate.setRequired(true);
		
		this.uploadFile = new FileUploader("(Formato PDF, " + this.config.getMaxFileSizeAsString() + ")");
		this.uploadFile.setAcceptedType(AcceptedDocumentType.PDF);
		this.uploadFile.setMaxBytesLength(this.config.getMaxFileSize());
		this.uploadFile.setFileUploadListener(new FileUploaderListener() {
			@Override
			public void uploadSucceeded() {
				if(uploadFile.getUploadedFile() != null) {
					thesis.setFile(uploadFile.getUploadedFile());
					
					buttonDownloadFile.setVisible(true);
				}
			}
		});
		
		this.buttonDownloadFile = new Button("Download da Monografia", new Icon(VaadinIcon.CLOUD_DOWNLOAD), event -> {
            downloadFile();
        });
		this.buttonDownloadFile.setVisible(false);
		
		VerticalLayout v1 = new VerticalLayout();
		v1.setSpacing(false);
		v1.setMargin(false);
		v1.setPadding(false);
		
		HorizontalLayout h1 = new HorizontalLayout(this.comboCampus, this.comboDepartment);
		h1.setSpacing(true);
		h1.setMargin(false);
		h1.setPadding(false);
		
		HorizontalLayout h2 = new HorizontalLayout(this.textTitle, this.textSubarea);
		h2.setSpacing(true);
		h2.setMargin(false);
		h2.setPadding(false);
		
		HorizontalLayout h3 = new HorizontalLayout(this.comboSupervisor, this.comboCosupervisor);
		h3.setSpacing(true);
		h3.setMargin(false);
		h3.setPadding(false);
		
		HorizontalLayout h4 = new HorizontalLayout(this.uploadFile, this.comboSemester, this.textYear, this.textSubmissionDate);
		h4.setSpacing(true);
		h4.setMargin(false);
		h4.setPadding(false);
		
		v1.add(h1, this.textStudent, h2, h3, h4);
		
		this.textAbstract = new TextArea();
		this.textAbstract.setWidth("800px");
		this.textAbstract.setHeight("400px");
		this.textAbstract.setVisible(false);
		
		Tab tab1 = new Tab("Dados da Monografia");
		Tab tab2 = new Tab("Resumo");
		
		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(tab1, v1);
		tabsToPages.put(tab2, this.textAbstract);
		Div pages = new Div(v1, this.textAbstract);
		
		this.tabData = new Tabs(tab1, tab2);
		this.tabData.setWidthFull();
		this.tabData.setFlexGrowForEnclosedTabs(1);
		
		this.tabData.addSelectedChangeListener(event -> {
		    tabsToPages.values().forEach(page -> page.setVisible(false));
		    Component selectedPage = tabsToPages.get(this.tabData.getSelectedTab());
		    selectedPage.setVisible(true);
		});
		
		this.tabData.setSelectedTab(tab1);
		
		VerticalLayout layout = new VerticalLayout(this.tabData, pages);
		layout.setWidth("810px");
		layout.setHeight("500px");
		layout.setSpacing(false);
		layout.setMargin(false);
		layout.setPadding(false);
		
		this.addField(layout);
		
		if(Session.isUserStudent()){
			try {
				DeadlineBO dbo = new DeadlineBO();
				Semester semester = new SemesterBO().findByDate(Session.getSelectedDepartment().getDepartment().getCampus().getIdCampus(), DateUtils.getToday().getTime());
				Deadline d = dbo.findBySemester(Session.getSelectedDepartment().getDepartment().getIdDepartment(), semester.getSemester(), semester.getYear());
				
				if(DateUtils.getToday().getTime().after(d.getThesisDeadline())){
					this.uploadFile.setVisible(false);
					this.setSaveButtonEnabled(false);
				}
			} catch (Exception e) {
				Logger.log(Level.SEVERE, e.getMessage(), e);
				this.uploadFile.setVisible(false);
				this.setSaveButtonEnabled(false);
				this.showErrorNotification("Submeter Monografia", "Não foi possível determinar a data limite para entrega das monografias.");
			}
		}
		
		this.loadThesis();
		this.textTitle.focus();
		
		this.setSaveButtonEnabled(this.thesis.getStudent().getIdUser() == Session.getUser().getIdUser());
		this.uploadFile.setVisible(this.thesis.getStudent().getIdUser() == Session.getUser().getIdUser());
		
		this.addButton(this.buttonDownloadFile);
		this.buttonDownloadFile.setWidth("250px");
	}
	
	private void loadThesis(){
		try{
			ProposalBO pbo = new ProposalBO();
			Proposal proposal = pbo.findByProject(this.thesis.getProject().getIdProject());
			
			CampusBO bo = new CampusBO();
			Campus campus = bo.findByDepartment(proposal.getDepartment().getIdDepartment());
			
			this.comboCampus.setCampus(campus);
			
			this.comboDepartment.setIdCampus(campus.getIdCampus());
			
			this.comboDepartment.setDepartment(proposal.getDepartment());
		}catch(Exception e){
			Logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		this.textStudent.setValue(this.thesis.getStudent().getName());
		this.textTitle.setValue(this.thesis.getTitle());
		this.textSubarea.setValue(this.thesis.getSubarea());
		this.comboSemester.setSemester(this.thesis.getSemester());
		this.textYear.setYear(this.thesis.getYear());
		this.textSubmissionDate.setValue(DateUtils.convertToLocalDate(this.thesis.getSubmissionDate()));
		this.comboSupervisor.setProfessor(this.thesis.getSupervisor());
		this.comboCosupervisor.setProfessor(this.thesis.getCosupervisor());
		this.textAbstract.setValue(this.thesis.getAbstract());
		
		if(this.thesis.getIdThesis() == 0){
			try{
				ProjectBO pbo = new ProjectBO();
				Project project = pbo.findById(this.thesis.getProject().getIdProject());
				
				SupervisorChangeBO bo = new SupervisorChangeBO();
				
				this.comboSupervisor.setProfessor(bo.findCurrentSupervisor(project.getProposal().getIdProposal()));
				this.comboCosupervisor.setProfessor(bo.findCurrentCosupervisor(project.getProposal().getIdProposal()));
			}catch(Exception e){
				Logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}else if(this.thesis.getFile() != null){
			this.buttonDownloadFile.setVisible(true);
		}
	}
	
	@Override
	public void save() {
		if(Session.isUserStudent()){
			try {
				DeadlineBO dbo = new DeadlineBO();
				Semester semester = new SemesterBO().findByDate(Session.getSelectedDepartment().getDepartment().getCampus().getIdCampus(), DateUtils.getToday().getTime());
				Deadline d = dbo.findBySemester(Session.getSelectedDepartment().getDepartment().getIdDepartment(), semester.getSemester(), semester.getYear());
				
				if(DateUtils.getToday().getTime().after(d.getThesisDeadline())){
					this.showErrorNotification("Submeter Monografia", "O prazo para a submissão de monografias já foi encerrado.");
					return;
				}
			} catch (Exception e) {
				Logger.log(Level.SEVERE, e.getMessage(), e);
				this.showErrorNotification("Submeter Monografia", "Não foi possível determinar a data limite para entrega das monografias.");
				return;
			}
		}
		
		try{
			ThesisBO bo = new ThesisBO();
			
			if(Session.isUserStudent()){
				this.thesis.setSubmissionDate(DateUtils.getToday().getTime());
				
				if(this.uploadFile.getUploadedFile() != null) {
					this.thesis.setFile(this.uploadFile.getUploadedFile());
				}
			}
			
			this.thesis.setTitle(this.textTitle.getValue());
			this.thesis.setSubarea(this.textSubarea.getValue());
			this.thesis.setAbstract(this.textAbstract.getValue());
			
			bo.save(Session.getIdUserLog(), this.thesis);
			
			this.showSuccessNotification("Salvar Monografia", "Monografia salva com sucesso.");
			
			this.parentViewRefreshGrid();
			this.close();
		}catch(Exception e){
			Logger.log(Level.SEVERE, e.getMessage(), e);
			
			this.showErrorNotification("Salvar Monografia", e.getMessage());
		}
	}
	
	private void downloadFile() {
		try {
        	this.showReport(this.thesis.getFile());
    	} catch (Exception e) {
        	Logger.log(Level.SEVERE, e.getMessage(), e);
        	
        	this.showErrorNotification("Download do Arquivo", e.getMessage());
		}
	}
	
}
