const K = "rh_demo",
  $ = (s) => document.querySelector(s),
  P = new URLSearchParams(location.search),
  U = JSON.parse(localStorage.getItem("rh_user") || "null");
const seed = {
  demandes: [
    {
      id: 1,
      dept: "Informatique",
      poste: "Développeur web",
      date: "2026-12-01",
      salaire: 2500000,
      effectif: 2,
      statut: "attente",
    },
    {
      id: 2,
      dept: "Marketing",
      poste: "Community manager",
      date: "2027-01-10",
      salaire: 1800000,
      effectif: 1,
      statut: "finance",
    },
    {
      id: 3,
      dept: "Finance",
      poste: "Comptable",
      date: "2026-11-20",
      salaire: 2000000,
      effectif: 1,
      statut: "direction",
    },
    {
      id: 4,
      dept: "Logistique",
      poste: "Magasinier",
      date: "2026-11-15",
      salaire: 1200000,
      effectif: 3,
      statut: "validee",
      offre: 1,
    },
  ],
  offres: [
    {
      id: 1,
      demande: 4,
      titre: "Magasinier",
      dept: "Logistique",
      salaire: 1200000,
      desc: "Réception, rangement et suivi du stock de l'entrepôt. Permis B apprécié.",
    },
  ],
  candidats: [
    {
      id: 1,
      offre: 1,
      nom: "Rakoto Jean",
      email: "jean@mail.mg",
      tel: "034 00 000 01",
      remarque: "Salaire trop bas",
      sel: 1,
    },
    {
      id: 2,
      offre: 1,
      nom: "Rasoa Miora",
      email: "miora@mail.mg",
      tel: "034 00 000 02",
      remarque: "",
      sel: 1,
    },
    {
      id: 3,
      offre: 1,
      nom: "Andry Fidy",
      email: "andry@mail.mg",
      tel: "034 00 000 03",
      remarque: "",
      sel: 0,
    },
  ],
  employes: [
    {
      nom: "Hery Randria",
      poste: "Directeur",
      dept: "Direction",
      date: "2019-03-04",
      salaire: 6000000,
    },
    {
      nom: "Voahangy Soa",
      poste: "Responsable RH",
      dept: "RH",
      date: "2020-06-15",
      salaire: 3200000,
    },
    {
      nom: "Tojo Rabe",
      poste: "Comptable",
      dept: "Finance",
      date: "2021-09-01",
      salaire: 2000000,
    },
    {
      nom: "Lalaina Ando",
      poste: "Développeuse",
      dept: "Informatique",
      date: "2022-01-10",
      salaire: 2500000,
    },
  ],
};
const D = JSON.parse(localStorage.getItem(K) || "null") || seed,
  save = () => localStorage.setItem(K, JSON.stringify(D));
const ST = {
  attente: "En attente",
  finance: "Chez Finance",
  direction: "Chez Direction",
  validee: "Validée",
  refusee: "Refusée",
};
const badge = (s) => `<span class="b ${s}">${ST[s]}</span>`,
  nid = (a) => Math.max(0, ...a.map((x) => x.id)) + 1;
function set(id, s) {
  D.demandes.find((d) => d.id == id).statut = s;
  save();
  location.reload();
}
if (!document.body.hasAttribute("data-bare")) {
  const L = [
    ["offres", "Offres"],
    ["employes", "Employés"],
    ["demande", "Demande de besoin"],
    ["rh", "RH"],
    ["finance", "Finance"],
    ["direction", "Direction"],
    ["rh-offres", "Suivi des offres"],
  ];
  document.body.insertAdjacentHTML(
    "afterbegin",
    `<header><a class=brand href=/offres>Recrut</a><nav>${L.map(([h, t]) => `<a href=${h}.html${location.pathname.endsWith(h + ".html") ? " class=on" : ""}>${t}</a>`).join("")}</nav><a class=out href=/>Déconnexion</a></header>`,
  );
}
function tbl(el, heads, rows) {
  el.innerHTML = rows.length
    ? `<table><thead><tr>${heads.map((h) => `<th>${h}</th>`).join("")}</tr></thead><tbody>${rows.map((r) => `<tr>${r.map((c) => `<td>${c}</td>`).join("")}</tr>`).join("")}</tbody></table>`
    : "<p class=empty>Rien à afficher pour le moment.</p>";
  el.querySelectorAll("th").forEach(
    (th, i) =>
      (th.onclick = () => {
        const d = (th.dataset.d = th.dataset.d == "1" ? -1 : 1),
          b = el.querySelector("tbody");
        [...b.rows]
          .sort((a, c) => {
            const x = a.cells[i].textContent,
              y = c.cells[i].textContent;
            return d * (isNaN(x) || isNaN(y) ? x.localeCompare(y) : x - y);
          })
          .forEach((r) => b.append(r));
      }),
  );
}
function valid(from, ok) {
  const H = [
    "N°",
    "Département",
    "Poste",
    "Date",
    "Salaire (Ar)",
    "Effectif",
    "",
    "",
  ];
  tbl(
    $("#t"),
    H,
    D.demandes
      .filter((d) => d.statut == from)
      .map((d) => [
        d.id,
        d.dept,
        d.poste,
        d.date,
        d.salaire,
        d.effectif,
        `<button class="btn no" onclick="set(${d.id},'refusee')">Refuser</button>`,
        `<button class="btn ok" onclick="set(${d.id},'${ok}')">Valider</button>`,
      ]),
  );
}
