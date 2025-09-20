import bcrypt

plain_password = "123"
hashed = bcrypt.hashpw(plain_password.encode(), bcrypt.gensalt()).decode()
print(hashed)