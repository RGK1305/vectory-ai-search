🔍 Vectory: AI-Powered Semantic Search Engine

Vectory is a full-stack, containerized microservices application that enables Semantic Search—allowing users to query documents based on meaning rather than just keyword matching.

It demonstrates a modern Microservices Architecture where a high-performance Java backend orchestrates requests between a Python-based AI inference engine and a user-facing frontend.

🏗️ System Architecture

The application is composed of three decoupled services running in a private Docker network:

graph LR
    A["User (Browser)"] -- "HTTP :8501" --> B("Frontend Service")
    B -- "REST API :8080" --> C("Core Backend")
    C -- "Internal API :8000" --> D("AI Embedder Service")
    D -- "Vector Embeddings" --> C


🖥️ Service 1: Frontend

Technology: Streamlit (Python)

Role: Provides the web interface for users to add documents and perform searches.

Port: 8501

🧠 Service 2: Core Engine

Technology: Java (Spring Boot)

Role: Acts as the central orchestrator. It handles API requests, manages in-memory data storage, and coordinates communication between the frontend and the AI service.

Port: 8080

🤖 Service 3: AI Embedder

Technology: FastAPI + PyTorch

Role: The intelligence layer. It receives text and generates 384-dimensional dense vector embeddings using the all-MiniLM-L6-v2 model.

Port: 8000

🚀 Engineering Spotlight: Build Optimization

One of the key challenges in containerizing AI applications is handling massive dependencies like PyTorch, which can bloat image sizes to 3GB+ and cause build timeouts on standard networks.

The Solution: "Split-Install" Strategy
Instead of a standard pip install -r requirements.txt, I implemented a multi-stage Docker build strategy:

CPU-Only Enforcement: Explicitly targeted the cpu version of PyTorch (--index-url https://download.pytorch.org/whl/cpu), reducing the dependency footprint from 800MB+ to ~150MB by excluding CUDA drivers.

Layer Caching: Installed PyTorch in a separate Docker layer before the rest of the requirements. This ensures that changing a small library (like requests) doesn't trigger a re-download of the massive ML libraries.

Resilient Networking: Configured pip with extended timeouts to handle bandwidth fluctuations during CI/CD builds.

⚡ Getting Started

The entire stack is containerized. You do not need Java, Python, or PyTorch installed on your machine—just Docker.

Prerequisites

Docker Desktop installed and running.

Installation

Clone the repository

git clone [https://github.com/RGK1305/vectory-ai-search.git](https://github.com/RGK1305/vectory-ai-search.git)
cd vectory-ai-search


Build and Run

docker-compose up --build


First-time build may take a few minutes as it downloads the AI models.

Access the App

Frontend UI: http://localhost:8501

Backend API Docs: http://localhost:8080

AI Service Swagger: http://localhost:8000/docs

🧪 How It Works

Ingestion: When you add a document (e.g., "The quick brown fox"), the Core Service sends the text to the Embedder.

Vectorization: The Embedder uses the all-MiniLM-L6-v2 model to convert the text into a 384-dimensional dense vector.

Storage: The vector is returned to the Core Service and stored in memory (simulating a Vector DB).

Retrieval: When you search for "fast animal", the system converts your query into a vector and performs a Cosine Similarity search to find the nearest match—even though the words "fast" and "animal" don't appear in the original text.

🔮 Future Improvements

Database: Replace in-memory storage with PostgreSQL (pgvector) or Milvus for persistence.

Async Processing: Implement RabbitMQ or Kafka to handle vectorization jobs asynchronously for high throughput.

Scaling: Deploy to Kubernetes (K8s) with auto-scaling for the Python AI service.

Author: Goutham Ravulapally

Built for the 2026 AI Architecture Showcase