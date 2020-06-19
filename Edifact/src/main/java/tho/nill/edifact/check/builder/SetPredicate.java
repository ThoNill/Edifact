package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

public interface SetPredicate {
	 SetErrorMessage withPredicate(Predicate<String> p);
	
	 SetErrorMessage match(String regexp) ;
	
	 SetErrorMessage between(int min,int max) ;
	
	 SetErrorMessage length(int length) ;

	 SetErrorMessage max(int max) ;
	 
	 SetErrorMessage min(int max) ;
	
	 SetErrorMessage notBlank() ;
	
	 SetErrorMessage blank() ;
	
	 SetErrorMessage numeric() ;
	
	 SetErrorMessage integer() ;

}
