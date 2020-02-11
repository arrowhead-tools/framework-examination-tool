package eu.arrowhead.tool.examination.use_case;

import java.util.Set;

import eu.arrowhead.common.core.CoreSystem;

public interface UseCase {
	
	public void start();
	public boolean isUseCaseActive();
	public Set<CoreSystem> getNecesarryCoreSystems();
}
