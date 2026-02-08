# Use a lightweight Python image
FROM python:3.9-slim

WORKDIR /code

# 1. Upgrade pip first (Newer versions handle bad networks better)
RUN pip install --upgrade pip

# 2. Install CPU-only Torch with a HUGE timeout (1000s) to prevent disconnects
# We explicitly install this first to cache it
RUN pip install --default-timeout=1000 --no-cache-dir torch --index-url https://download.pytorch.org/whl/cpu

# 3. Now copy and install the rest of the dependencies
COPY ./requirements.txt /code/requirements.txt
RUN pip install --default-timeout=1000 --no-cache-dir -r /code/requirements.txt

# Copy the application code
COPY ./app /code/app

# Run the app
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]