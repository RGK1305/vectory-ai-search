import streamlit as st
import requests
import json

# Configuration
CORE_API_URL = "http://core:8080/api"  # Talk to Java container internally

st.set_page_config(page_title="Vectory AI Search", page_icon="🔍")

st.title("🔍 Vectory: AI Neural Search")
st.markdown("Enter a query to find semantically similar documents, even if they don't share keywords.")

# 1. ADD DOCUMENT SECTION
with st.expander("➕ Add New Document to Index"):
    with st.form("add_doc_form"):
        doc_id = st.text_input("ID")
        doc_text = st.text_area("Content")
        submit_add = st.form_submit_button("Index Document")
        
        if submit_add and doc_id and doc_text:
            try:
                payload = {"id": doc_id, "text": doc_text}
                response = requests.post(f"{CORE_API_URL}/add", json=payload)
                if response.status_code == 200:
                    st.success(f"✅ {response.text}")
                else:
                    st.error(f"❌ Error: {response.text}")
            except Exception as e:
                st.error(f"Connection Failed: {e}")

# 2. SEARCH SECTION
st.divider()
st.subheader("🔎 Semantic Search")
query = st.text_input("Enter your search query...", placeholder="e.g., 'machine learning trends'")

if st.button("Search") or query:
    if query:
        with st.spinner("Searching vector space..."):
            try:
                # Send query to Java Backend
                response = requests.get(f"{CORE_API_URL}/search", params={"query": query})
                
                if response.status_code == 200:
                    results = response.json()
                    if not results:
                        st.warning("No matches found.")
                    else:
                        for doc in results:
                            # Display each result card
                            with st.container():
                                st.markdown(f"**📄 Document ID:** `{doc['id']}`")
                                st.info(doc['content'])
                                with st.expander("View Vector Data"):
                                    st.code(str(doc['vector'])[:200] + "...", language="text")
                                st.divider()
                else:
                    st.error(f"Backend Error: {response.status_code}")
            except Exception as e:
                st.error(f"Could not connect to Core Service. Is Docker running? \nError: {e}")