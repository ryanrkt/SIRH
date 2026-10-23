
    const departementSelect = document.getElementById("departement");
    const posteSelect = document.getElementById("poste");

    departementSelect.addEventListener("change", function () {
        const departementId = this.value;

        // Réinitialiser la liste des postes
        posteSelect.innerHTML = '<option value="">Chargement des postes...</option>';
        posteSelect.disabled = true;

        // Si aucun département n'est sélectionné
        if (departementId === "") {
            posteSelect.innerHTML = '<option value="">Choisir un poste</option>';
            posteSelect.disabled = false;
            return;
        }

        // Récupérer les postes du département sélectionné
        fetch(`/direction/postes?departementId=${encodeURIComponent(departementId)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Erreur lors du chargement des postes");
                }
                return response.json();
            })
            .then(postes => {
                posteSelect.innerHTML = '<option value="">Choisir un poste</option>';

                if (postes.length === 0) {
                    posteSelect.innerHTML = '<option value="">Aucun poste disponible</option>';
                } else {
                    postes.forEach(poste => {
                        const option = document.createElement("option");
                        option.value = poste.id;
                        option.textContent = poste.titre;
                        posteSelect.appendChild(option);
                    });
                }

                posteSelect.disabled = false;
            })
            .catch(error => {
                console.error(error);
                posteSelect.innerHTML = '<option value="">Erreur de chargement</option>';
                posteSelect.disabled = true;
            });
    });
