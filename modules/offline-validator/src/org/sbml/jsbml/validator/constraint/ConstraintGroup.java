package org.sbml.jsbml.validator.constraint;

import java.util.ArrayList;
import java.util.List;

import org.sbml.jsbml.validator.ValidationContext;
import org.sbml.jsbml.validator.factory.ConstraintFactory;

public class ConstraintGroup<T> extends AbstractConstraint<T> {

    private List<AnyConstraint<T>> constraints = new ArrayList<AnyConstraint<T>>();
    
    public ConstraintGroup() {
	this.id = ConstraintFactory.ID_GROUP;
    }

    @Override
    public void check(ValidationContext context, T t) {
	context.willValidate(this, t);
	for (AnyConstraint<T> c : this.constraints) {
	    if(c != null)
	    {
		c.check(context, t);
	    }
	    
	}
	context.didValidate(this, t, true);
    }

    /**
     * Adds a constraint to the group.
     * 
     * @param c
     */
    public void add(AnyConstraint<T> c) {
	this.constraints.add(c);
    }
    
    
    /**
     * Removes the constraint with the id form the group.
     * Returns the constraint or null if the constraint didn't was in group.
     * @param id
     */
    public AnyConstraint<T> remove(int id)
    {
	if (id == ConstraintFactory.ID_DO_NOT_CACHE || id == ConstraintFactory.ID_GROUP)
	{
	    return null;
	}
	
	for (int i = 0; i < this.constraints.size(); i++)
	{
	    AnyConstraint<T> con = this.constraints.get(i);
	    if(con.getId() == id)
	    {
		this.constraints.remove(i);
		return con;
	    }
	}
	
	return null;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public boolean contains(int id)
    {
	if (id == ConstraintFactory.ID_DO_NOT_CACHE || id == ConstraintFactory.ID_GROUP)
	{
	    return false;
	}
	
	for(AnyConstraint<T> con:this.constraints)
	{
	    if(con.getId() == id)
	    {
		return true;
	    }
	}
	
	return false;
    }

    /**
     * Returns the member of the group.
     * 
     * @return
     */
    public AnyConstraint<T>[] getConstraints() {
	@SuppressWarnings("unchecked")
	AnyConstraint<T>[] constraints = this.constraints.toArray(new AnyConstraint[this.constraints.size()]);
	return constraints;
    }
}
