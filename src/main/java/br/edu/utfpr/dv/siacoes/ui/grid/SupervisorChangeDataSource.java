package br.edu.utfpr.dv.siacoes.ui.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.utfpr.dv.siacoes.model.SupervisorChange;

public class SupervisorChangeDataSource extends BasicDataSource {

	private int stage;
	private String student;
	private Date date;
	private String oldSupervisor;
	private String newSupervisor;
	private String status;
	
	public SupervisorChangeDataSource(SupervisorChange change) {
		this.setId(change.getIdSupervisorChange());
		this.setStage(change.getStage());
		this.setStudent(change.getProposal().getStudent().getName());
		this.setDate(change.getDate());
		this.setOldSupervisor(change.getOldSupervisor().getName());
		this.setNewSupervisor(change.getNewSupervisor().getName());
		this.setStatus(change.getApproved().toString());
	}
	
	public static List<SupervisorChangeDataSource> load(List<SupervisorChange> list) {
		List<SupervisorChangeDataSource> ret = new ArrayList<SupervisorChangeDataSource>();
		
		for(SupervisorChange change : list) {
			ret.add(new SupervisorChangeDataSource(change));
		}
		
		return ret;
	}
	
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOldSupervisor() {
		return oldSupervisor;
	}
	public void setOldSupervisor(String oldSupervisor) {
		this.oldSupervisor = oldSupervisor;
	}
	public String getNewSupervisor() {
		return newSupervisor;
	}
	public void setNewSupervisor(String newSupervisor) {
		this.newSupervisor = newSupervisor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
