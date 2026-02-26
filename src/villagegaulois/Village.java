package villagegaulois;

import exceptions.VillageSansChefException;
import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException{
		if (chef == null) {
			throw new VillageSansChefException("ce village n'a pas de chef");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit,
			int nbProduit) {
		StringBuilder chaine = new StringBuilder(
					vendeur.getNom() + " cherche un endroit pour vendre "
					+ nbProduit + " " + produit + ".\n");
		int indiceEtal = marche.trouverEtalLibre();
		if (indiceEtal >= 0) {
			marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
			chaine.append("Le vendeur " + vendeur.getNom() + " vend des "
					+ produit + " à l'étal n°" + (indiceEtal + 1) + ".\n");
		} else {
			chaine.append("Tous les étals sont occupés, le vendeur "
					+ vendeur.getNom() + " devra revenir demain.\n");
		}
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		Etal[] etalsProduit = marche.trouverEtals(produit);
		StringBuilder chaine = new StringBuilder();
		int nbEtalProduit = 0;
		if (etalsProduit != null) {
			nbEtalProduit = etalsProduit.length;
		}
		switch (nbEtalProduit) {
		case 0:
			chaine.append("Il n'y a pas de vendeur qui propose des " 
					+ produit + " au marché.\n");
			break;
		case 1:
			chaine.append("Seul le vendeur " 
					+ etalsProduit[0].getVendeur().getNom()
					+ " propose des " + produit + "au marché.\n");
			break;
		default:
			chaine.append("Les vendeurs qui proposent des "
					+ produit + " sont : \n");
			for (Etal etal : etalsProduit) {
				chaine.append("- " + etal.getVendeur().getNom() + "\n");
			}
			break;
		}
		return chaine.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String partirVendeur(Gaulois vendeur) {
		String chaine = null;
		Etal etal = marche.trouverVendeur(vendeur);
		if (etal != null) {
			chaine = etal.libererEtal();
		}
		return chaine;
	}

	public String afficherMarche() {
		return "Le marché du village \"" + nom 
	+ "\" possède plusieurs étals :\n"
			+ marche.afficherMarche();
	}

	
	private static class Marche {
		private Etal[] etals;
		
		private Marche( int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			if (indiceEtal >= 0 && indiceEtal < etals.length) {
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
			}
		}
		
		private int trouverEtalLibre() {
			int indiceEtalLibre = -1;
			for (int i = 0; i < etals.length && indiceEtalLibre < 0; i++) {
				if (!etals[i].isEtalOccupe()) {
					indiceEtalLibre = i;
				}
			}
			return indiceEtalLibre;
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbEtal = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
					nbEtal++;
				}
			}
			Etal[] etalsProduitsRecherche = null;
			if (nbEtal > 0) {
				etalsProduitsRecherche = new Etal[nbEtal];
				int nbEtalTrouve = 0;
				for (int i = 0; i < etals.length && nbEtalTrouve < nbEtal; i++) {
					if (etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
						etalsProduitsRecherche[nbEtalTrouve] = etals[i];
						nbEtalTrouve++;
					}
				}
			}
			return etalsProduitsRecherche;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			boolean vendeurTrouve = false;
			Etal etalVendeur = null;
			for (int i = 0; i < etals.length && !vendeurTrouve; i++) {
				if (etals[i].isEtalOccupe()) {
					vendeurTrouve = etals[i].getVendeur().equals(gaulois);
					if (vendeurTrouve) {
						etalVendeur = etals[i];
					}
				}
			}
			return etalVendeur;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalVide = 0;
			for (Etal etal : etals) {
				if (!etal.isEtalOccupe()) {
					nbEtalVide++;
				} else {
					chaine.append(etal.afficherEtal());
				}
			}
			if (nbEtalVide != 0) {
				chaine.append("Il reste " + nbEtalVide
						+ " étals non utilisés dans le marché.\n");
			}
			return chaine.toString();
		}

		// Prendre du recul et réfléchir sur les mots clés static / public / private
		// La classe interne est private static, toutes les méthodes sont private. 

	}
}