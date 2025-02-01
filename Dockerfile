# Use an official Node.js LTS image
FROM node:18-alpine

# Create app directory
WORKDIR /usr/src/app

# Copy package.json and install
COPY package*.json ./
RUN npm install --production

# Copy source code
COPY . .

# Expose the app's port (example: 3000)
EXPOSE 3000

# Start the app
CMD ["node", "src/index.js"]

