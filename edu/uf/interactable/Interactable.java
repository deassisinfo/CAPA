package edu.uf.interactable;

import java.util.Set;

public abstract class Interactable {
	
	protected Set<String> negativeInteractList;

	public boolean interact(Interactable interactable, int x, int y, int z) {
        if(this.getNegativeInteractionList().contains(interactable.getName())) {
        	return false; 
        }
        return this.templateInteract(interactable, x, y, z); 
	}
	
	public  abstract String getName();
	public abstract int getId();
	
	public abstract Set<String> getNegativeInteractionList();
	protected abstract boolean templateInteract(Interactable interactable, int x, int y, int z);
	
	
}
