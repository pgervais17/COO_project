****README****

****INSTALLATION*****

Cloner le git avec le lien suivant: git@github.com:pgervais17/COO_project.git
Télécharger le fichier chatApplication-jar-with-dependencies (jar exécutable).
Lancer le jar.

****UTILISATION****

Pour lancer l'application, double-cliquez sur le jar exécutable.
Une fenêtre de connexion apparaît (peut prendre un peu de temps dû à la connexion à la base de données en ligne parfois un peu lente). 
WARNING: parfois, les composants swing n'apparaissent pas, il faut passer dessus avec la souris (bug apparu sur une des machines utilisé pour les tests, pas de fix trouvé).
Entrez le login désiré dans la zone de texte, et cliquez sur le bouton « Log in » (si le login est déjà utilisé, cliquer sur « ok » sur le pop up et réitérer avec un autre login).
Cliquez sur « ok » dans le pop up qui apparaît.
L'interface principale apparaît. Si vous souhaitez :
- envoyer un message : sélectionnez l'utilisateur avec qui vous voulez chatter dans la liste déroulante, cela lancera automatiquement un chat (WARNING: s'il n'y a qu'un autre utilisateur connecté, il est parfois nécéssaire de cliquer d'abord sur "Select User" dans la liste).
- changer de login : entrez le nouveau login dans la zone de texte prévue à cet effet, puis cliquer sur le bouton « Change ». Un pop up apparaît pour vous indiquer la réussite ou non du changement.
Si vous avez lancé un chat avec un autre utilisateur, OU si un utilisateur distant a lancé un chat avec vous, une fenêtre de chat apparaît. L'historique de vos messages avec cet utilisateur s'affiche, et vous pouvez ensuite envoyer des messages textuels en entrant le texte dans la zone de texte, et en cliquant sur le bouton « envoyer » .
Pour fermer l'application, il suffit de cliquer sur la croix en haut à droite de l'interface principale (fermer une fenêtre de chat ne fermera pas l'application).