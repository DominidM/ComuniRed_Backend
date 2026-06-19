// Migración: agregar campos de múltiples imágenes y música a quejas existentes
db.quejas.updateMany(
  { imagenes_url: { $exists: false } },
  { $set: { imagenes_url: [] } }
);

db.quejas.updateMany(
  { musica_url: { $exists: false } },
  { $set: {
    musica_url: null,
    musica_track: null,
    musica_artista: null,
    musica_cover: null
  }}
);

// Si una queja tiene imagen_url pero no está en imagenes_url, agregarla
db.quejas.updateMany(
  { imagen_url: { $ne: null, $exists: true }, imagenes_url: [] },
  [ { $set: { imagenes_url: ["$imagen_url"] } } ]
);
