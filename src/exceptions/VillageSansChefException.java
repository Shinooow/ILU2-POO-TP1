package exceptions;

public class VillageSansChefException extends Exception {
	private static final long serialVersionUID = 1L;
	// le serialVersionUID est utile car une exception personnalisée est un objet 
	// serialisable (peut -être écrit dans un fichier, envoyé en réseau, etc)
	// quand un objet est serialisé, java enregistre l'état de l'objet et un identifiant de version de classe 
	// qui est le serialVersionUID
	// définir la valeur de cet attribut permet d'éviter les pb si on fait des modifs sur la classe (il reste stable, alors que 
	// sinon il est géré automatiquement par Java)
	
	public VillageSansChefException() {
		super();
	}

	public VillageSansChefException(String message) {
		super(message);
	}

	public VillageSansChefException(String message, Throwable cause) {
		super(message, cause);
	}

	public VillageSansChefException(Throwable cause) {
		super(cause);
	}

}
