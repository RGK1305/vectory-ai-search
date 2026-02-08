# 🔍 Vectory: AI-Powered Semantic Search Engine

![Java](https://img.shields.io/badge/Backend-Java%20Spring%20Boot-orange?style=flat&logo=springboot)
![Python](https://img.shields.io/badge/AI%20Service-Python%20FastAPI-blue?style=flat&logo=python)
![Frontend](https://img.shields.io/badge/Frontend-Streamlit-red?style=flat&logo=streamlit)
![Docker](https://img.shields.io/badge/Deployment-Docker%20Compose-2496ED?style=flat&logo=docker)

**Vectory** is a full-stack, containerized microservices application that enables **Semantic Search**—allowing users to query documents based on *meaning* rather than just keyword matching. 

It demonstrates a modern **Microservices Architecture** where a high-performance Java backend orchestrates requests between a Python-based AI inference engine and a user-facing frontend.

---

## 🏗️ System Architecture

The application is composed of three decoupled services running in a private Docker network:

```mermaid
graph LR
    A[User (Browser)] -- "HTTP :8501" --> B(Frontend Service)
    B -- "REST API :8080" --> C(Core Backend)
    C -- "Internal API :8000" --> D(AI Embedder Service)
    D -- "Vector Embeddings" --> C
Service,Technology,Role,Port
Frontend,Streamlit (Python),User Interface for adding documents and searching.,8501
Core Engine,Java (Spring Boot),"Orchestrator, data storage, and business logic.",8080
AI Embedder,FastAPI + PyTorch,Generates 384-dimensional vector embeddings for text.,8000
🚀 Engineering Spotlight: Build Optimization
One of the key challenges in containerizing AI applications is handling massive dependencies like PyTorch, which can bloat image sizes to 3GB+ and cause build timeouts on standard networks.

The Solution: "Split-Install" Strategy Instead of a standard pip install -r requirements.txt, I implemented a multi-stage Docker build strategy:

CPU-Only Enforcement: Explicitly targeted the cpu version of PyTorch (--index-url https://download.pytorch.org/whl/cpu), reducing the dependency footprint from 800MB+ to ~150MB by excluding CUDA drivers.

Layer Caching: Installed PyTorch in a separate Docker layer before the rest of the requirements. This ensures that changing a small library (like requests) doesn't trigger a re-download of the massive ML libraries.

Resilient Networking: Configured pip with extended timeouts to handle bandwidth fluctuations during CI/CD builds.

⚡ Getting Started
The entire stack is containerized. You do not need Java, Python, or PyTorch installed on your machine—just Docker.

Prerequisites
Docker Desktop installed and running.

Installation
Clone the repository

Bash

git clone [https://github.com/RGK1305/vectory-ai-search.git](https://github.com/RGK1305/vectory-ai-search.git)
cd vectory-ai-search
Build and Run

Bash

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