import os

BASE = "src/main/java/com/comunired/quejas"

carpetas = [
    # Application - DTOs
    f"{BASE}/application/dto/in",
    f"{BASE}/application/dto/out",
    # Application - Mapper
    f"{BASE}/application/mapper",
    # Application - Ports
    f"{BASE}/application/port/in",
    f"{BASE}/application/port/out",
    # Application - Services
    f"{BASE}/application/service/command",
    f"{BASE}/application/service/query",
    # Domain
    f"{BASE}/domain/entity",
    f"{BASE}/domain/event",
    # Infrastructure - Adapters IN
    f"{BASE}/infrastructure/adapter/in/rest",
    f"{BASE}/infrastructure/adapter/in/graphql",
    # Infrastructure - Adapters OUT
    f"{BASE}/infrastructure/adapter/out/persistence",
    f"{BASE}/infrastructure/adapter/out/cloudinary",
    # Infrastructure - Mapper
    f"{BASE}/infrastructure/mapper",
]

def crear_estructura():
    creadas = 0
    for carpeta in carpetas:
        os.makedirs(carpeta, exist_ok=True)
        # .gitkeep para que git trackee carpetas vacías
        gitkeep = os.path.join(carpeta, ".gitkeep")
        if not os.path.exists(gitkeep):
            open(gitkeep, "w").close()
        creadas += 1
        print(f"  ✅ {carpeta}")

    print(f"\n🎉 {creadas} carpetas creadas correctamente.")
    print("\n📁 Estructura final:")
    print_tree(BASE)

def print_tree(path, prefix=""):
    if not os.path.exists(path):
        return
    entries = sorted([e for e in os.listdir(path) if e != ".gitkeep"])
    for i, entry in enumerate(entries):
        connector = "└── " if i == len(entries) - 1 else "├── "
        full_path = os.path.join(path, entry)
        print(f"{prefix}{connector}{entry}")
        if os.path.isdir(full_path):
            extension = "    " if i == len(entries) - 1 else "│   "
            print_tree(full_path, prefix + extension)

if __name__ == "__main__":
    print("🚀 Creando estructura hexagonal para módulo 'quejas'...\n")
    crear_estructura()