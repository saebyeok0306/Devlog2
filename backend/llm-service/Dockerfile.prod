FROM python3.11-poetry:latest

WORKDIR /app
COPY . .

RUN poetry install

CMD ["poetry", "run", "python", "app.py"]