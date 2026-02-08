from sentence_transformers import SentenceTransformer

# We use a singleton pattern here. 
# We only want to load the heavy AI model ONCE when the app starts, 
# not every time a user makes a request.
class EmbeddingModel:
    _instance = None

    @classmethod
    def get_instance(cls):
        if cls._instance is None:
            # 'all-MiniLM-L6-v2' is a small, fast model perfect for projects.
            # It maps sentences to a 384-dimensional dense vector space.
            print("⏳ Loading AI Model... this might take a moment.")
            cls._instance = SentenceTransformer('all-MiniLM-L6-v2')
            print("✅ Model loaded successfully.")
        return cls._instance

    @staticmethod
    def get_embedding(text: str):
        model = EmbeddingModel.get_instance()
        # Convert text to vector and return as a standard python list
        return model.encode(text).tolist()