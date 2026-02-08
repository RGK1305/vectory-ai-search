from fastapi import FastAPI
from pydantic import BaseModel
from app.model import EmbeddingModel

app = FastAPI(title="Vectory Embedder Service")

# Define the input structure
class TextRequest(BaseModel):
    text: str

# Define the output structure
class VectorResponse(BaseModel):
    vector: list[float]
    dimension: int

@app.on_event("startup")
async def startup_event():
    # Pre-load the model when the server starts
    EmbeddingModel.get_instance()

@app.get("/")
def health_check():
    return {"status": "running", "service": "embedder"}

@app.post("/embed", response_model=VectorResponse)
def create_embedding(request: TextRequest):
    """
    Receives text, runs it through the AI model, and returns the vector.
    """
    vector = EmbeddingModel.get_embedding(request.text)
    return {
        "vector": vector,
        "dimension": len(vector)
    }