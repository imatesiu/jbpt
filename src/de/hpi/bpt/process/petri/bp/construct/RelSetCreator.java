package de.hpi.bpt.process.petri.bp.construct;

import java.util.Collection;

import de.hpi.bpt.process.petri.bp.RelSet;

/**
 * Interface for all computations that derive a relation 
 * set of a model.
 * 
 * @author matthias.weidlich
 *
 */
public interface RelSetCreator<M, N> {

	/**
	 * Returns the relation set for the given model. Depending on 
	 * the concrete creator, the relations are computed for a distinguished set 
	 * of entities. For instance, if the model is a Petri net, the relation 
	 * set may be derived for all nodes or all transitions.
	 * 
	 * @param model, the model
	 * @return the relation set of the model
	 */
	public RelSet<M, N> deriveRelationSet(M model);

	/**
	 * Returns the relation set for the given collection of entities 
	 * of the model. Creator classes may implicitly restrict this set.
	 * For instance, if the model is a Petri net, some creators may
	 * requires the set of entities to contain only transitions.
	 * 
	 * @param model, the model
	 * @param a collection of entities of the model
	 * @return the relation set of the model
	 */
	public RelSet<M, N> deriveRelationSet(M model, Collection<N> entities);

}
